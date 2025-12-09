'use client';

import { useRouter } from 'next/navigation';

import { Title, Card, Text, Stack } from '@mantine/core';

import { GradientButton } from './GradientButton';
interface Leilao {
  id: number;
  nomeDoProduto: string;
  descricao: string;
  lanceInicial: number;
  dataInicio: string;
  dataFim: string;
}

export function LeilaoCard({ leilao }: { leilao: Leilao }) {
  const router = useRouter();

  return (
    <Card shadow="sm" radius="md" withBorder>
      <Card.Section withBorder inheritPadding py="xs">
        <Stack gap="xs">
          <Title order={3}>
            {leilao.nomeDoProduto}
          </Title>
          <Text size="sm"  lineClamp={2}>
            {leilao.descricao}
          </Text>
        </Stack>
      </Card.Section>

      <Stack gap="xs" mt="md">
        <Text size="sm" >
          Valor inicial: R$ {leilao.lanceInicial.toFixed(2)}
        </Text>
        <Text size="sm" >
          Início: {new Date(leilao.dataInicio).toLocaleString('pt-BR')}
        </Text>
        <Text size="sm" >
          Fim: {new Date(leilao.dataFim).toLocaleString('pt-BR')}
        </Text>

        <GradientButton text='Ver detalhes' onClick={() => router.push(`/leilao/${leilao.id}`)}/>
      </Stack>
    </Card>
  );
}
