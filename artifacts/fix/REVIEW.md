# Implementação do fix.md

Esta rodada corresponde ao pedido em `../../fix.md`. O relatório de `../ui-redesign/` descreve a entrega anterior.

## Arquitetura e fluxo

Foram reutilizados `NewQuoteWizardScreen`, `QuotesScreen`, `BruleeApp`, o repositório Room, `SavedRecipe`, os motores de custo/preço e os componentes do tema. O formulário mantém os campos com `rememberSaveable`; a navegação conserva estados em `SaveableStateHolder`, com chaves distintas para orçamento novo e cada rascunho. O ViewModel continua responsável pela navegação e pelo repositório, sem criar outro fluxo de orçamento.

`QuoteDraft`, no domínio, guarda a receita, ingredientes, unidades, rendimento, tempos, complexidade, embalagem, outros custos, tarifas utilizadas, margem, preço manual e etapa. `QuoteDraftCodec`, na camada de dados, serializa esse snapshot para a coluna opcional `draftData`. Cliente, contato, data, validade e observações continuam nos campos existentes do orçamento. A migração Room 4 → 5 preserva os registros anteriores.

Em **Pedidos → Continuar orçamento**, o formulário recebe o orçamento existente e restaura seus dados. Salvar novamente faz upsert com o mesmo ID e código. Aprovar usa o fluxo de produção existente. Ao criar um orçamento novo, o estado da edição anterior é descartado. Ingredientes em edição precisam ser salvos ou cancelados antes de avançar ou salvar o rascunho.

Rascunhos legados sem snapshot permitem editar cliente, data e demais dados disponíveis e mantêm seus totais. A interface informa que os ingredientes e tempos antigos não estão disponíveis; para recalcular, é necessário preenchê-los. Não é possível reconstruir informações que as versões anteriores não armazenavam.

## Interface e componentes

- `DeliveryDateField`: calendário Material 3, datas anteriores desabilitadas, confirmação de hoje ou futuro e formatação `dd/MM/aaaa`. A entrada manual permanece disponível, inclusive para horários de orçamentos antigos. A data acompanha o estado salvo do formulário.
- `QuotesScreen`: lista lazy com chaves estáveis, filtros, estado vazio, cards compactos com código, cliente, produto/quantidade, preço, entrega, status e ação principal. Ações secundárias ficam em menu. Status de produção distinguem aprovado, em produção, concluído e cancelado. Ao retomar um rascunho ou mudar de destino, avisos antigos são dispensados para liberar os botões da próxima tela.
- `BruleeTransition`: `AnimatedContent` com slide curto e fade, direção conforme avanço/retorno. `BruleeAnimatedAmount` faz fade dos valores. Ambos respeitam movimento reduzido.
- `BruleeMascot`: adaptação nativa da referência `anima`, que é um arquivo HTML com SVG e animações CSS, sem Lottie/GIF/WebP. Flutuação discreta, piscadas, movimento do chapéu e sombra; resposta à mudança de etapa e estados de processamento/sucesso. Sem nova dependência.
- `BruleeLogo`: arte oficial transparente no fundo da marca, usada no cabeçalho. Os quatro arquivos oficiais PNG/SVG foram examinados; recursos raster adequados foram derivados para o Android.
- Launcher: ícones normais e redondos de 48, 72, 96, 144 e 192 px, foreground e background adaptativos em `mipmap-anydpi-v26`; splash do Android 12+ usa a mesma identidade.
- Cards de ingredientes usam informação e ações em linhas separadas para acomodar nomes longos sem esconder editar/excluir.

## Arquivos principais

UI: `app/src/main/kotlin/com/bruleetelie/app/ui/BruleeApp.kt`, `screens/NewQuoteWizardScreen.kt`, `screens/QuotesScreen.kt`, novo `screens/DeliveryDateField.kt`, `components/BruleeMotion.kt`, `components/BruleeMascot.kt` e novo `components/BruleeLogo.kt`.

Persistência: `domain/.../QuoteAndOrderModels.kt`, `data/.../QuoteDraftCodec.kt`, `data/.../Mappers.kt`, `data/.../db/Entities.kt`, `data/.../db/BruleeDatabase.kt` e schema 5.

Marca: `app/src/main/AndroidManifest.xml`, `app/src/main/res/drawable-nodpi/`, `mipmap-{mdpi,hdpi,xhdpi,xxhdpi,xxxhdpi}/`, `mipmap-anydpi-v26/`, `values/colors.xml` e `values-v31/`.

Testes: novos `DraftQuoteE2eTest`, `QuoteUiRegressionTest`, `DeliveryDateTest`, `QuoteDraftCodecTest`; ajustes em `WizardRegressionTest`, `OperationalScreensRegressionTest`, `QuoteFlowE2eTest` e `BruleeDatabaseMigrationTest`.

## Validação

Validação concluída no AVD `Medium_Phone`, imagem Android API 37.2, com janela visível:

- **34 testes Compose**, sem falhas ou casos ignorados: criação, avanço/retorno, calendário, recriação da Activity, salvar/reabrir/editar o mesmo rascunho, aprovação, produção, venda, WhatsApp, PDF, backup, lista vazia e 120 pedidos, estados de produção, movimento reduzido e rascunhos legados.
- **3 testes instrumentados de dados**, incluindo migração 4 → 5 e persistência após reabrir o repositório.
- **79 testes unitários únicos**, 105 execuções entre variantes, sem falhas.
- **3 testes da verificação de distribuição**, além da inspeção do APK: nenhum banco empacotado ou código de fixtures no app.
- Builds **debug e release** aprovados. **Lint aprovado**, com zero erros; 39 avisos estão registrados nos XMLs, sem supressões adicionadas nesta rodada.
- Instalação limpa auditada com **zero registros nas 21 tabelas de negócio**. Ao terminar, os dados do app foram apagados novamente com `pm clear`; somente o pacote principal permanece instalado.

Os XMLs e logs estão em `results/`; a consolidação está em [validation-summary.json](validation-summary.json). A suíte Compose rodou antes da regressão unitária. O primeiro comando que combinava build release e lint encontrou uma falha do analisador ao ler um arquivo Hilt durante sua regeneração; executar build/testes e depois lint em comandos separados passou, sem desabilitar verificações.

APK validado: [brulee-debug.apk](brulee-debug.apk), versão **0.2.0 / código 2**. O script `../../scripts/install-clean-debug.sh` usa esse APK por padrão.

SHA-256: `b1f0aabe5a30a39a47f0f252ca9e878295c34ba64d112e577b4f4f50e35991f1`.

Evidências: [inicialização vazia com a marca](clean-start.png) e [contagem das tabelas](clean-database.json). O WhatsApp é validado por interceptação de Intent; os testes não enviam mensagens reais. A animação foi validada no emulador; não foi feito benchmark em aparelho físico.

A rodada inicial executou 26 testes de Compose e encontrou duas falhas: identificação semântica do dia desabilitado do calendário e um aviso de rascunho salvo sobreposto ao botão de aprovação. A rodada seguinte ampliou a cobertura para 33 casos e também detectou a necessidade de aguardar o aviso de aprovação antes de finalizar a produção no teste. Seus XMLs estão em `results/compose-initial/` e `results/compose-second/`; essas rodadas não são apresentadas como aprovadas. A rodada final, após as correções e a inclusão do teste de rascunho legado, passou os 34 casos.
