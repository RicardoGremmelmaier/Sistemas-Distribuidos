'use client';

import { useEffect } from 'react';
import { Alert, Transition } from '@mantine/core';
import { IconCheck, IconX, IconAlertCircle } from '@tabler/icons-react';

interface NotificationBannerProps {
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  visible: boolean;
  extend?: number;
  onClose: () => void;
}

export function NotificationBanner({ type, message, visible, extend = 4000, onClose }: NotificationBannerProps) {
  const icons = {
    success: <IconCheck size={18} />,
    error: <IconX size={18} />,
    warning: <IconAlertCircle size={18} />,
    info: <IconAlertCircle size={18} />,
  };

  const colors = {
    success: 'green',
    error: 'red',
    warning: 'yellow',
    info: 'blue',
  };

  useEffect(() => {
    if (visible) {
      const timer = setTimeout(() => {
        onClose();
      }, extend);
      return () => clearTimeout(timer);
    }
  }, [visible, onClose, extend]);

  return (
    <Transition
      mounted={visible}
      transition="slide-down"
      duration={300}
      timingFunction="ease"
    >
      {(styles) => (
        <div style={{ position: 'fixed', top: 20, right: 20, zIndex: 2000, ...styles }}>
          <Alert
            icon={icons[type]}
            title={type.toUpperCase()}
            color={colors[type]}
            withCloseButton
            onClose={onClose}
            variant="filled"
          >
            {message}
          </Alert>
        </div>
      )}
    </Transition>
  );
}
