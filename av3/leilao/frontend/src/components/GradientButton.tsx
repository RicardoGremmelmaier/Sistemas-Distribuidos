import { Button } from '@mantine/core';

interface GradientButtonProps {
  text: string;
  onClick?: () => void;
}

export function GradientButton({ text, onClick }: GradientButtonProps) {
  return (
    <Button
      variant="gradient"
      gradient={{ from: 'green', to: 'cyan', deg: 90 }}
      onClick={onClick}
    >
      {text}
    </Button>
  );
}
