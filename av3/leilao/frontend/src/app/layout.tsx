'use client';

import '@mantine/core/styles.css';
import '@mantine/dates/styles.css';
import '@/styles/globals.css';

import { MantineProvider } from '@mantine/core';

import { theme } from '@/styles/theme';
import { BasicAppShell } from '@/components/BasicAppShell';


export default function RootLayout({ children }: { children: React.ReactNode }) {   

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
