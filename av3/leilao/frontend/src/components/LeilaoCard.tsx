'use client';

import { Card, Text, Button, Group, Stack } from '@mantine/core';
import Link from 'next/link';

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
        <Text fw={600} size="lg">
          {leilao.nomeProduto}
        </Text>
        <Text size="sm" c="dimmed" lineClamp={2}>
          {leilao.descricao}
        </Text>
        <Text size="sm" fw={500}>
          Valor inicial: R$ {leilao.valorInicial.toFixed(2)}
        </Text>
        <Text size="xs" c="dimmed">
          Início: {new Date(leilao.dataInicio).toLocaleString('pt-BR')}
        </Text>
        <Text size="xs" c="dimmed">
          Fim: {new Date(leilao.dataFim).toLocaleString('pt-BR')}
        </Text>

        <Group justify="flex-end" mt="sm">
          <Link href={`/leilao/${leilao.id}`}>
            <Button variant="filled" color="brand.5" size="compact-md">
              Ver detalhes
            </Button>
          </Link>
        </Group>
      </Stack>
    </Card>
  );
}
