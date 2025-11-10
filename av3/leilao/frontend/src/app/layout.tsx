'use client';

import '@mantine/core/styles.css';
import '@/styles/globals.css';

import { MantineProvider, AppShell, Container } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';

import { Header } from '@/components/Header';
import { theme } from '@/styles/theme';
import { BasicAppShell } from '@/components/BasicAppShell';


export default function RootLayout({ children }: { children: React.ReactNode }) {
    const [mobileOpened, {toggle: toggleMobile}] = useDisclosure();
    const [desktopOpened, {toggle: toggleDesktop}] = useDisclosure(true);   

    return (
    <html lang="pt-BR">
      <body>
        <MantineProvider theme={theme}>
          <BasicAppShell> {children} </BasicAppShell>
        </MantineProvider>
      </body>
    </html>
  );
}
