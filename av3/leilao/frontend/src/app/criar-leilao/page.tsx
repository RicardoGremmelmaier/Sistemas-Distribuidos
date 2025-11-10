'use client';

import { useState } from 'react';
import { TextInput, NumberInput, Textarea, Button, Card, Title, Group } from '@mantine/core';
import { DateTimePicker } from '@mantine/dates';
import { notifications } from '@mantine/notifications';
import { GradientButton } from '@/components/GradientButton';

import axios from 'axios';
import dayjs from 'dayjs';

export default function CriarLeilaoPage() {
  const [formData, setFormData] = useState({
    nomeProduto: '',
    descricao: '',
    valorInicial: 0,
    dataInicio: new Date(),
    dataFim: new Date(),
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (field: string, value: any) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    const payload = {
        nomeProduto: formData.nomeProduto,
        descricao: formData.descricao,
        valorInicial: formData.valorInicial,
        dataInicio: dayjs(formData.dataInicio).format('YYYY-MM-DDTHH:mm:ssZ'),
        dataFim: dayjs(formData.dataFim).format('YYYY-MM-DDTHH:mm:ssZ'),
    };


    try {
      await axios.post('http://localhost:8080/leiloes', payload);
      notifications.show({
        title: 'Sucesso!',
        message: 'Leilão criado com sucesso.',
        color: 'green',
      });
      setFormData({
        nomeProduto: '',
        descricao: '',
        valorInicial: 0,
        dataInicio: new Date(),
        dataFim: new Date(),
      });
    } catch (error: any) {
        if (axios.isAxiosError(error)) {
            console.error("Erro na requisição Axios:");
            console.error("Status:", error.response?.status);
            console.error("Mensagem:", error.response?.data?.message);
            console.error("Erro completo:", error.response?.data);
            console.error("Payload enviado:", payload);
        } else {
            console.error("Erro inesperado:", error);
        }

        notifications.show({
            title: 'Erro',
            message: `Falha ao criar leilão: ${error.response?.data?.message || 'Erro desconhecido'}`,
            color: 'red',
        });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex justify-center items-center w-full mt-10">
      <Card shadow="sm" radius="md" withBorder className="w-[600px]">
        <Title order={3} mb="md">
          Criar novo Leilão
        </Title>

        <form onSubmit={handleSubmit}>
          <TextInput
            label="Nome do Produto"
            placeholder="Ex: PlayStation 5"
            value={formData.nomeProduto}
            onChange={(e) => handleChange('nomeProduto', e.currentTarget.value)}
            required
            mb="sm"
          />

          <Textarea
            label="Descrição"
            placeholder="Ex: Edição digital, novo, com garantia..."
            value={formData.descricao}
            onChange={(e) => handleChange('descricao', e.currentTarget.value)}
            minRows={3}
            mb="sm"
          />

          <NumberInput
            label="Valor inicial (R$)"
            prefix='R$ '
            value={formData.valorInicial}
            onChange={(v) => handleChange('valorInicial', v || 0)}
            min={0}
            decimalScale={2}
            fixedDecimalScale
            thousandSeparator=" "
            allowNegative={false}
            mb="sm"
            required
          />

          <Group grow mb="sm">
            <DateTimePicker
              dropdownType='modal'
              label="Data de início"
              value={formData.dataInicio}
              onChange={(v) => handleChange('dataInicio', v)}
              required
            />
            <DateTimePicker
              dropdownType='modal'
              label="Data de fim"
              value={formData.dataFim}
              onChange={(v) => handleChange('dataFim', v)}
              required
            />
          </Group>

          <GradientButton text={loading ? 'Criando...' : 'Criar Leilão'} type='submit' disabled={loading} />
        </form>
      </Card>
    </div>
  );
}
