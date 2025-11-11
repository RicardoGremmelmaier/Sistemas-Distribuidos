"use client";

import { useEffect, useState, useRef } from "react";
import { useParams } from "next/navigation";
import { useDisclosure } from "@mantine/hooks";

import { Card, Title, Text, Group, Button, Stack, Loader } from "@mantine/core";

import { NotificationBanner } from "@/components/NotificationBanner";
import { GradientButton } from "@/components/GradientButton";
import { LanceModal } from "@/components/LanceModal";

import axios from "axios";

interface Leilao {
  id: number;
  nomeDoProduto: string;
  descricao: string;
  lanceInicial: number;
  dataInicio: string;
  dataFim: string;
  status: string;
}

export default function LeilaoDetalhesPage() {
  const { id } = useParams();
  const leilaoId = Number(id);
  const eventSourceRef = useRef<EventSource | null>(null);

  const [clienteId, setClienteId] = useState<number | null>(null);

  const [leilao, setLeilao] = useState<Leilao | null>(null);
  const [maiorLance, setMaiorLance] = useState<number | null>(null);
  const [inscrito, setInscrito] = useState(false);
  const [loading, setLoading] = useState(true);

  const [notif, setNotif] = useState({
    visible: false,
    type: "info" as "success" | "error" | "warning" | "info",
    message: "",
  });

  const [modalOpened, { open: openModal, close: closeModal }] =
    useDisclosure(false);

  const showNotification = (
    type: "success" | "error" | "warning" | "info",
    message: string
  ) => {
    setNotif({ visible: true, type, message });
  };

  const fetchData = async () => {
    if (clienteId === null) return;
    setLoading(true);
    try {
      const leilaoRes = await axios.get<Leilao>(
        `http://localhost:8080/leiloes/${leilaoId}`
      );
      const lanceRes = await axios.get<number>(
        `http://localhost:8080/lances/maior/${leilaoId}`
      );
      const inscRes = await axios.get<boolean>(
        `http://localhost:8080/notificacoes/inscrito/${leilaoId}/${clienteId}`
      );
      setLeilao(leilaoRes.data);
      setMaiorLance(lanceRes.data || leilaoRes.data.lanceInicial);
      setInscrito(inscRes.data);
    } catch (err) {
      showNotification("error", "Erro ao carregar os dados do leilão.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    let storedId = sessionStorage.getItem("clienteId");
    if (!storedId) {
      storedId = String(Math.floor(Math.random() * 1_000_000));
      sessionStorage.setItem("clienteId", storedId);
    }
    setClienteId(Number(storedId));
  }, []);

  useEffect(() => {
    if (clienteId !== null) {
      fetchData();
    }
  }, [leilaoId, clienteId]);

  // Funções SSE
  const inscrever = () => {
    if (eventSourceRef.current) return;

    const es = new EventSource(
      `http://localhost:8080/notificacoes/subscribe/${leilaoId}/${clienteId}`
    );
    eventSourceRef.current = es;
    setInscrito(true);
    showNotification(
      "success",
      "Você está recebendo notificações deste leilão."
    );

    es.onmessage = (event) => {
      console.log("Evento SSE recebido:", event.data);
      showNotification("info", `Novo evento: ${event.data}`);
      fetchData();
    };

    es.onerror = (error) => {
      console.error("Erro SSE:", error);
      es.close();
      eventSourceRef.current = null;
      setInscrito(false);
      showNotification("error", "Conexão de notificações perdida.");
    };
  };

  const cancelarInscricao = async () => {
    try {
      await axios.delete(
        `http://localhost:8080/notificacoes/unsubscribe/${leilaoId}/${clienteId}`
      );
      eventSourceRef.current?.close();
      eventSourceRef.current = null;
      setInscrito(false);
      showNotification("warning", "Você parou de receber notificações.");
    } catch (err) {
      console.error(err);
      showNotification("error", "Erro ao cancelar inscrição.");
    }
  };

  if (clienteId === null) {
    return (
      <div className="flex justify-center items-center h-[70vh]">
        <Loader size="lg" />
      </div>
    );
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-[70vh]">
        <Loader size="lg" />
      </div>
    );
  }

  if (!leilao) {
    return (
      <div className="flex justify-center items-center h-[70vh]">
        <Text color="red">Leilão não encontrado.</Text>
      </div>
    );
  }

  return (
    <div className="flex justify-center items-center w-full mt-10">
      <NotificationBanner
        type={notif.type}
        message={notif.message}
        visible={notif.visible}
        onClose={() => setNotif({ ...notif, visible: false })}
      />

      <LanceModal
        opened={modalOpened}
        onClose={closeModal}
        clienteId={clienteId}
        leilaoId={leilaoId}
        onLanceSuccess={fetchData}
        onNotify={showNotification}
      />

      <Card shadow="sm" radius="md" withBorder className="w-[600px]">
        <Card.Section withBorder inheritPadding py="xs">
          <Stack>
            <Title order={1}>{leilao.nomeDoProduto}</Title>
            <Text size="lg">{leilao.descricao}</Text>
          </Stack>
        </Card.Section>

        <Stack gap="xs" mt="md">
          <Group grow>
            <Text size="sm">Status: {leilao.status}</Text>
            <Text size="sm">
              Valor inicial: R$ {leilao.lanceInicial.toFixed(2)}
            </Text>
          </Group>

          <Group grow>
            <Text size="sm">
              Início: {new Date(leilao.dataInicio).toLocaleString("pt-BR")}
            </Text>
            <Text size="sm">
              Fim: {new Date(leilao.dataFim).toLocaleString("pt-BR")}
            </Text>
          </Group>

          <Text size="sm">
            Maior lance atual: R$ {maiorLance?.toFixed(2) ?? "—"}
          </Text>
          <Group grow>
            <Button
              variant="gradient"
              gradient={
                !inscrito
                  ? { from: "green", to: "cyan", deg: 90 }
                  : { from: "red", to: "orange", deg: 90 }
              }
              onClick={inscrito ? cancelarInscricao : inscrever}
            >
              {inscrito ? "Parar notificações" : "Receber notificações"}
            </Button>
            <GradientButton
              text="Dar Lance"
              onClick={openModal}
            ></GradientButton>
          </Group>
        </Stack>
      </Card>
    </div>
  );
}
