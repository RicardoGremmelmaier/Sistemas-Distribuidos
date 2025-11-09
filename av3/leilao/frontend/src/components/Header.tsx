'use client';

import { Group, Text, Button, Container } from '@mantine/core';
import Link from 'next/link';

export function Header() {
  return (
    <Container size="lg" py="sm">
      <Group justify="space-between" align="center">
        <Link href="/">
          <Text size="xl" fw={600} c="brand.6">
            Sistema de Leilões
          </Text>
        </Link>

        <Group>
          <Link href="/notificacoes">
            <Button variant="light" color="brand.5">
              Notificações
            </Button>
          </Link>
        </Group>
      </Group>
    </Container>
  );
}
