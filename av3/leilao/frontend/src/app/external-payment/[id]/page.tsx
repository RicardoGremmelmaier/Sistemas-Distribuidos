'use client';

import { useParams, useSearchParams } from 'next/navigation';
import { useState } from 'react';
import axios from 'axios';
import { Button, Card, Title, Text, Group, Stack } from '@mantine/core';

import { GradientButton } from '@/components/GradientButton';

export default function ExternalPaymentPage() {
  const { id } = useParams();
  const searchParams = useSearchParams();

  const leilaoId = Number(searchParams.get('leilaoId'));
  const valor = Number(searchParams.get('valor'));
  const clienteId = Number(searchParams.get('clienteId'));

  const [status, setStatus] = useState<'PENDENTE' | 'APROVADO' | 'RECUSADO'>('PENDENTE');
  const [msg, setMsg] = useState('');

  const enviarCallback = async (novoStatus: 'APROVADO' | 'RECUSADO') => {
    try {
      setStatus(novoStatus);
      await axios.post('http://localhost:8084/external-payment/finalizar', {
        idTransacao: id,
        leilaoId,
        valor,
        status: novoStatus,
        dadosCliente: { clienteId },
      });
      setMsg(`Pagamento ${novoStatus === 'APROVADO' ? 'aprovado' : 'recusado'} com sucesso!`);
    } catch (err) {
      console.error(err);
      setMsg('Erro ao enviar callback.');
    }
  };

  return (
    <div className="flex justify-center items-center h-[80vh]">
      <Card shadow="md" padding="xl" radius="lg" withBorder className="w-[400px]">
        <Stack align="center">
          <Title order={2}>Pagamento Externo</Title>
          <Text>Transação: {id}</Text>
          <Text>Leilão: {leilaoId}</Text>
          <Text>Valor: R$ {valor.toFixed(2)}</Text>
          <Text>Cliente: {clienteId}</Text>

          <Group mt="md">
            <GradientButton
              text='Aprovar'
              gradientType="greenToCyan"
              disabled={status !== 'PENDENTE'}
              onClick={() => enviarCallback('APROVADO')}
            >
            </GradientButton>
            <GradientButton
              text='Recusar'
              gradientType="redToOrange"
              disabled={status !== 'PENDENTE'}
              onClick={() => enviarCallback('RECUSADO')}
            >
            </GradientButton>
          </Group>

          {msg && (
            <Text
              mt="md"
              c={status === 'APROVADO' ? 'green' : status === 'RECUSADO' ? 'red' : 'gray'}
            >
              {msg}
            </Text>
          )}
        </Stack>
      </Card>
    </div>
  );
}
