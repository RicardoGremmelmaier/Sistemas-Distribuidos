'use client';

import { MantineProvider, AppShell, Container } from '@mantine/core';
import { Header } from '@/components/Header';
import { theme } from '@/styles/theme';
import '@/styles/globals.css';

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-BR">
      <body>
        <MantineProvider theme={theme}>
          <AppShell
            header={{ height: 60 }}
            padding="md"
            styles={{
              main: {
                backgroundColor: '#f8f9fa',
                minHeight: '100vh',
              },
            }}
          >
            <AppShell.Header>
              <Header />
            </AppShell.Header>

            <AppShell.Main>
              <Container size="md">{children}</Container>
            </AppShell.Main>
          </AppShell>
        </MantineProvider>
      </body>
    </html>
  );
}
