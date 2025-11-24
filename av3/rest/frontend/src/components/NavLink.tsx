import { useRouter } from 'next/navigation';

import {NavLink } from '@mantine/core';

import { IconHome, IconGavel } from '@tabler/icons-react';

export function CustomNavLink() {
    const router = useRouter();
  return (
    <>
      <NavLink
        label="Página Inicial"
        leftSection={<IconHome size={16} stroke={1.5} color='red' />}
        color='black'
        variant='subtle'
        active
        onClick={() => router.push('/')}
      />
      <NavLink
        label="Criar leilão"
        leftSection={<IconGavel size={16} stroke={1.5} color='blue' />}
        color='black'
        variant='subtle'
        active
        onClick={() => router.push('/criar-leilao')}
      />

    </>
  );
}