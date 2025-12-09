'use client';

import { useEffect, useState } from 'react';
import { SimpleGrid, Title, Loader, Center, Alert } from '@mantine/core';
import { LeilaoCard } from '@/components/LeilaoCard';
import axios from 'axios';

interface Leilao {
  id: number;
  nomeDoProduto: string;
  descricao: string;
  lanceInicial: number;
  dataInicio: string;
  dataFim: string;
}

export default function HomePage() {
  const [leiloes, setLeiloes] = useState<Leilao[]>([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    axios
      .get<Leilao[]>('http://localhost:8080/leiloes/ativos')
      .then((res) => setLeiloes(res.data))
      .catch((err) => {
         console.error("Erro ao carregar leilões:", err);
        setErro('Não foi possível carregar os leilões.');
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading)
    return (
      <Center h="60vh">
        <Loader color="brand.5" size="lg" />
      </Center>
    );

  if (erro)
    return (
      <Center h="60vh">
        <Alert color="red">{erro}</Alert>
      </Center>
    );

  return (
    <>
      <Title order={2} mb="lg">
        Leilões Ativos
      </Title>

      <SimpleGrid cols={{ base: 1, sm: 2, md: 3 }} spacing="lg">
        {leiloes.length > 0 ? (
          leiloes.map((leilao) => <LeilaoCard key={leilao.id} leilao={leilao} />)
        ) : (
          <Center h="40vh">
            <Alert color="gray">Nenhum leilão disponível no momento.</Alert>
          </Center>
        )}
      </SimpleGrid>
    </>
  );
}
