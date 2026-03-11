import { bootProductDetail } from './app';

const ACCESS_TOKEN = 'DEMO_TOKEN_V1_B5_HCI_TZ6FFKOLVB';
const ENVIRONMENT = 'demo' as const;

// Boot default product on load
bootProductDetail(2525, ACCESS_TOKEN, ENVIRONMENT);

declare global {
  interface Window {
    setProductNumber: (productNumber: number) => void;
  }
}

window.setProductNumber = (productNumber: number) => {
  bootProductDetail(productNumber, ACCESS_TOKEN, ENVIRONMENT);
};
