import { Button } from '@mantine/core';

interface GradientButtonProps {
  text: string;
  gradientType?: 'greenToCyan' | 'redToOrange';
  onClick?: () => void;
  type?: 'button' | 'submit' | 'reset';
  disabled?: boolean;
}

export function GradientButton({ text, onClick, type = 'button', disabled, gradientType = "greenToCyan" }: GradientButtonProps) {
  const gradients = {
    greenToCyan: { from: 'green', to: 'cyan', deg: 90 },
    redToOrange: { from: 'red', to: 'orange', deg: 90 },
  };

  return (
    <Button
      variant="gradient"
      gradient={gradients[gradientType]}
      onClick={onClick}
      disabled={disabled}
      type={type}
    >
      {text}
    </Button>
  );
}
