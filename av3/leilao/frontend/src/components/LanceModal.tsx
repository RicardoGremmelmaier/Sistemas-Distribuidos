import { Modal, Button, Stack, Group, NumberInput } from '@mantine/core';

import { useState } from 'react';

import { GradientButton } from './GradientButton';
import { NotificationBanner } from './NotificationBanner';

import axios from 'axios';

interface LanceModalProps {
  opened: boolean;
  onClose: () => void;
  clienteId: number;
  leilaoId: number;
}

export function LanceModal({ opened, onClose, clienteId, leilaoId }: LanceModalProps) {
    const [formData, setFormData] = useState({
        clienteId: clienteId,
        leilaoId: leilaoId,
        lance: 0,
    });

    const [notification, setNotification] = useState<{
        type: 'success' | 'error' | 'warning' | 'info';
        message: string;
        visible: boolean;
    }>({ type: 'info', message: '', visible: false });

    const handleChange = (field: string, value: any) => {
        setFormData((prev) => ({ ...prev, [field]: value }));
    };

    const handleCloseNotification = () =>
        setNotification((prev) => ({ ...prev, visible: false }));

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setNotification((prev) => ({ ...prev, visible: false }));

        const payload = {
            clienteId: formData.clienteId,
            leilaoId: formData.leilaoId,
            lance: formData.lance,
        };

        try {
            await axios.post('http://localhost:8080/lances', payload);

            setNotification({
                type: 'success',
                message: 'Lance realizado com sucesso!',
                visible: true,
            });

            setFormData({
                clienteId: clienteId,
                leilaoId: leilaoId,
                lance: 0,
            });
            } catch (error: any) {
            console.error('Erro ao realizar lance:', error);
            setNotification({
                type: 'error',
                message:
                error.response?.data?.message ||
                'Falha ao realizar o lance. Verifique os dados e tente novamente.',
                visible: true,
            });
        }
    };

    return (
        <>
            <NotificationBanner
                type={notification.type}
                message={notification.message}
                visible={notification.visible}
                onClose={handleCloseNotification}
            />
            <Modal opened={opened} onClose={onClose} title="Dar Lance" centered>
                <Stack>
                    <form onSubmit={handleSubmit}>
                    <NumberInput
                        label="Valor (R$)"
                        prefix="R$ "
                        value={formData.lance}
                        onChange={(v) => handleChange('lance', v || 0)}
                        min={0}
                        decimalScale={2}
                        fixedDecimalScale
                        thousandSeparator=" "
                        allowNegative={false}
                        mb="sm"
                        required
                    />
                    </form>
                    <Group grow>
                        <GradientButton text='Confirmar Lance' onClick={onClose} />
                        <Button variant="outline" onClick={onClose}>Cancelar</Button>
                    </Group>
                </Stack>
            </Modal>
        </>
    );
}