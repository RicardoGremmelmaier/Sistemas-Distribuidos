'use client';

import { Title, Card, Text, Stack } from '@mantine/core';

import { GradientButton } from './GradientButton';
interface Leilao {
  id: number;
  nomeProduto: string;
  descricao: string;
  valorInicial: number;
  dataInicio: string;
  dataFim: string;
}

export function LeilaoCard({ leilao }: { leilao: Leilao }) {
  return (
    <Card shadow="sm" radius="md" withBorder>
      <Stack gap="xs">
        <Title order={3}>
          {leilao.nomeProduto}
        </Title>
        <Text size="sm"  lineClamp={2}>
          {leilao.descricao}
        </Text>
        <Text size="sm" >
          Valor inicial: R$ {leilao.valorInicial.toFixed(2)}
        </Text>
        <Text size="sm" >
          Início: {new Date(leilao.dataInicio).toLocaleString('pt-BR')}
        </Text>
        <Text size="sm" >
          Fim: {new Date(leilao.dataFim).toLocaleString('pt-BR')}
        </Text>

        <GradientButton text='Ver detalhes' onClick={() => console.log(`Ver detalhes do leilão ${leilao.id}`)}/>
      </Stack>
    </Card>
  );
}
