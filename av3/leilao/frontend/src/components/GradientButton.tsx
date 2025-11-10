import { Button } from '@mantine/core';

interface GradientButtonProps {
  text: string;
  onClick?: () => void;
  type?: 'button' | 'submit' | 'reset';
  disabled?: boolean;
}

export function GradientButton({ text, onClick, type = 'button', disabled }: GradientButtonProps) {
  return (
    <Button
      variant="gradient"
      gradient={{ from: 'green', to: 'cyan', deg: 90 }}
      onClick={onClick}
      disabled={disabled}
      type={type}
    >
      {text}
    </Button>
  );
}
