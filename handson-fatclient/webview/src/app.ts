import { registerAllComponents, type Environment } from '@documedis/web-components';

// Must be called once before using any @documedis/web-components custom elements
registerAllComponents();

export function bootProductDetail(
  productNumber: number,
  accessToken: string,
  environment: Environment,
) {
  const container = document.getElementById('product-detail-container')!;

  // Set all attributes at once via innerHTML — avoids the race condition
  // where attributeChangedCallback fires before access-token is set
  container.innerHTML = `
    <product-details
      access-token='${JSON.stringify(accessToken)}'
      environment='${JSON.stringify(environment)}'
      product-number='${JSON.stringify(productNumber)}'
      language='${JSON.stringify('de')}'
    ></product-details>
  `;
}
