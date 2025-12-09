import { Modal, Button, Stack, Group, NumberInput } from "@mantine/core";

import { useState } from "react";

import { GradientButton } from "./GradientButton";
import { NotificationBanner } from "./NotificationBanner";

import axios from "axios";

interface LanceModalProps {
  opened: boolean;
  onClose: () => void;
  clienteId: number;
  leilaoId: number;
  valorMinimo: number;
  onLanceSuccess: () => void;
  onNotify: (
    type: "success" | "error" | "warning" | "info",
    message: string
  ) => void;
}

export function LanceModal({
  opened,
  onClose,
  clienteId,
  leilaoId,
  valorMinimo,
  onLanceSuccess,
  onNotify,
}: LanceModalProps) {
  const [formData, setFormData] = useState({
    clienteId: clienteId,
    leilaoId: leilaoId,
    valor: 0,
  });

  const [isValorValido, setIsValorValido] = useState(false);

  const handleChange = (field: string, value: any) => {
    const novoValor = value || 0;
    setFormData((prev) => ({ ...prev, [field]: novoValor }));
    setIsValorValido(novoValor >= valorMinimo);
  };

  const [notification, setNotification] = useState<{
    type: "success" | "error" | "warning" | "info";
    message: string;
    visible: boolean;
  }>({ type: "info", message: "", visible: false });

  const handleCloseNotification = () =>
    setNotification((prev) => ({ ...prev, visible: false }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setNotification((prev) => ({ ...prev, visible: false }));

    const payload = {
      clienteId: formData.clienteId,
      leilaoId: formData.leilaoId,
      valor: formData.valor,
    };

    try {
      await axios.post("http://localhost:8080/lances", payload);

      onNotify("success", "Lance realizado com sucesso!");
      onLanceSuccess();

      onClose();
    } catch (error: any) {
      console.error("Erro ao realizar valor:", error);
      onNotify(
        "error",
        error.response?.data?.message || "Erro ao realizar lance."
      );
    }
  };

  return (
    <>
      <NotificationBanner
        type={notification.type}
        message={notification.message}
        visible={notification.visible}
        onClose={handleCloseNotification}
      />
      <Modal opened={opened} onClose={onClose} title="Dar Lance" centered>
        <Stack>
          <form onSubmit={handleSubmit}>
            <NumberInput
              label="Valor (R$)"
              prefix="R$ "
              value={formData.valor}
              onChange={(v) => handleChange("valor", v || 0)}
              min={valorMinimo}
              decimalScale={2}
              fixedDecimalScale
              thousandSeparator=" "
              allowNegative={false}
              mb="sm"
              required
            />
            <Group grow>
              <GradientButton text="Confirmar Lance" type="submit" disabled={!isValorValido}/>
              <Button
                variant="gradient"
                gradient={{ from: "red", to: "orange", deg: 90 }}
                onClick={onClose}
              >
                Cancelar
              </Button>
            </Group>
          </form>
        </Stack>
      </Modal>
    </>
  );
}
