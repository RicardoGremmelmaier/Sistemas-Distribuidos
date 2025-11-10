'use client';

import { Group, Burger, Title } from '@mantine/core';
import { GradientButton } from './GradientButton';
import { useDisclosure } from '@mantine/hooks';


export function Header() {
  const [opened, { toggle }] = useDisclosure();

  return (
    <Group h="100%" px="md" justify="space-between" gap="xl">
      <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm" />
      <Title order={2}>Sistema de Leilões</Title>
      <GradientButton text='Notificações'></GradientButton>
    </Group>
  );
}
