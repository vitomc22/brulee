# Brûlée Telie

Aplicativo Android local-first para custos, precificação, orçamentos, produção e análise financeira de confeitaria artesanal. Os valores monetários usam centavos inteiros; o histórico de vendas preserva os custos orçados e reais.

O app inicia sem dados de demonstração. Cadastre a primeira receita em **Novo Orçamento** ou pelo botão da bancada.

- **Orçamento:** ingredientes, conversões de unidade, rendimento, tempos, complexidade, embalagem, outros custos, margem e preço manual. Receitas são salvas em revisões e podem ser reutilizadas.
- **Pedidos:** lista compacta com status, rascunhos editáveis, orçamentos preparados para WhatsApp, aprovação e recusa. Use **Continuar orçamento** para restaurar a receita, os custos e os dados do cliente e salvar no mesmo registro. A data de entrega pode ser escolhida no calendário. O WhatsApp abre com telefone e mensagem do cliente; o envio é confirmado pelo usuário no aplicativo externo. PDFs do cliente incluem contato, quantidade, entrega, validade e observações, sem custos, margens ou lucro.
- **Produção:** pedidos reais, filtros de data, checklist persistente, custos e tempo ativo reais na conclusão. Aprovar ou finalizar novamente não duplica pedidos ou vendas.
- **Estoque:** cadastro de ingredientes e preços, importação de texto, scanner e PDF com revisão humana.
- **Finanças:** receita, lucro, conversão, lucro por hora, meta, ponto de equilíbrio, tendências de preços e exportação CSV/JSON. O gráfico mostra barras de receita e lucro desde a primeira venda, inclusive quando há prejuízo.
- **Configurações:** tarifas, margem padrão, despesas fixas, meta, movimento reduzido e backup portátil com validação de integridade.
- **Interação:** transições de etapas e navegação, progresso e checklist animados, botões com resposta ao toque e mascote animado. A preferência de movimento reduzido é respeitada. A inspiração em jogos se limita à linguagem visual; não há XP, níveis ou ofensivas.

## Desenvolvimento e validação

Módulos: `app`, `domain`, `data`, `documents` e `benchmark`. Use JDK 17, o SDK Android configurado em `local.properties` e o wrapper Gradle do projeto.

A validação de UI deve rodar no AVD **Medium_Phone**, com imagem **API 37.2**, seguido da regressão unitária:

```bash
./gradlew :app:connectedDebugAndroidTest :data:connectedDebugAndroidTest :documents:connectedDebugAndroidTest
./gradlew test
./gradlew :app:assembleDebug :app:assembleRelease
./gradlew lint
```

Os testes de tela criam suas próprias fixtures e limpam banco e preferências antes e depois de cada caso, inclusive em falhas. Dados fictícios ficam exclusivamente no APK de testes. Os testes de telas isoladas usam estado em memória; testes de persistência fecham o banco temporário em `finally`. Após a validação, o app instalado no emulador deve ficar com os dados limpos.

O relatório e o APK da implementação de `fix.md` ficam em [artifacts/fix/REVIEW.md](artifacts/fix/REVIEW.md). As revisões anteriores ficam em `artifacts/ui-redesign/` e `artifacts/functional-review/`.

A distribuição debug desativa o backup automático e a transferência de dados pelo Android para impedir a restauração de cadastros de testes. Instalar uma atualização preserva dados locais existentes; para uma instalação vazia, limpe os dados do app antes de abri-lo. O backup manual em ZIP continua disponível.

Para instalar a distribuição de revisão com a base vazia no dispositivo conectado, execute `./scripts/install-clean-debug.sh`. Esse comando instala o APK e apaga os registros locais anteriores do Brûlée Telie.
