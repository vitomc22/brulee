# Brûlée Telie

Aplicativo Android local-first para custos, precificação, orçamentos, produção e análise financeira de confeitaria artesanal. Os valores monetários usam centavos inteiros; o histórico de vendas preserva os custos orçados e reais.

O app inicia sem dados de demonstração. Cadastre a primeira receita em **Novo Orçamento** ou pelo botão da bancada.

- **Orçamento:** ingredientes, conversões de unidade, rendimento, tempos, complexidade, embalagem, outros custos, margem e preço manual. Receitas são salvas em revisões e podem ser reutilizadas.
- **Pedidos:** rascunhos, orçamentos preparados para WhatsApp, aprovação e recusa. O WhatsApp abre com telefone e mensagem do cliente; o envio é confirmado pelo usuário no aplicativo externo. PDFs do cliente não incluem custos, margens ou lucro.
- **Produção:** pedidos reais, filtros de data, checklist persistente, custos e tempo ativo reais na conclusão. Aprovar ou finalizar novamente não duplica pedidos ou vendas.
- **Estoque:** cadastro de ingredientes e preços, importação de texto, scanner e PDF com revisão humana, e inclusão de itens na lista de compras.
- **Finanças:** receita, lucro, conversão, lucro por hora, meta, ponto de equilíbrio, tendências de preços e exportação CSV/JSON. O painel funciona também sem vendas.
- **Configurações:** tarifas, margem padrão, despesas fixas, meta, sons, movimento reduzido e backup portátil com validação de integridade.
- **Interação:** transições de etapas e navegação, progresso e checklist animados, botões com resposta ao toque e mascote animado. A preferência de movimento reduzido é respeitada.

## Desenvolvimento e validação

Módulos: `app`, `domain`, `data`, `documents` e `benchmark`. Use JDK 17, o SDK Android configurado em `local.properties` e o wrapper Gradle do projeto.

A validação de UI deve rodar no AVD **Medium_Phone**, com imagem **API 37.2**, seguido da regressão unitária:

```bash
./gradlew :app:connectedDebugAndroidTest :data:connectedDebugAndroidTest
./gradlew test
./gradlew :app:assembleDebug :app:assembleRelease
./gradlew lint
```

Os testes de tela criam suas próprias fixtures e limpam banco e preferências antes e depois de cada caso, inclusive em falhas. Dados fictícios ficam exclusivamente no APK de testes. Os testes de telas isoladas usam estado em memória; testes de persistência fecham o banco temporário em `finally`. Após a validação, o app instalado no emulador deve ficar com os dados limpos.

Os resultados da revisão funcional de outubro de 2026 ficam em `artifacts/functional-review/`.
