import {NavLink } from '@mantine/core';
import { IconHome, IconGavel } from '@tabler/icons-react';

export function CustomNavLink() {
  return (
    <>
      <NavLink
        href="#required-for-focus"
        label="Página Inicial"
        leftSection={<IconHome size={16} stroke={1.5} color='red' />}
        color='black'
        variant='subtle'
        active
      />
      <NavLink
        href="#required-for-focus"
        label="Criar leilão"
        leftSection={<IconGavel size={16} stroke={1.5} color='blue' />}
        color='black'
        variant='subtle'
        active
      />

    </>
  );
}