# Revisão funcional Android — 20/10/2026

A revisão confrontou o código com as decisões e o roadmap recuperados do ai-memory. Os registros históricos de conclusão não correspondiam aos fluxos reais: havia ações sem efeito, dados fictícios em produção e testes que verificavam apenas a abertura de telas.

## Correções e regressões

| Problema | Correção | Cobertura |
| --- | --- | --- |
| WhatsApp apenas salvava o orçamento | Intent real com mensagem codificada, DDD/código de país e tratamento de falha; confirmação do envio fica no WhatsApp | `QuoteFlowE2eTest`, `WizardRegressionTest`, `DocumentGatewayTest` |
| Compartilhamento gerava outra identidade de orçamento | Identidade estável entre PDF, WhatsApp e aprovação; salvamento por upsert | `WizardRegressionTest`, `BruleeRepositoryTest` |
| Produção exibindo encomendas e indicadores fixos | Bancada alimentada pelo Room, estado vazio e criação de orçamento acessível | `ProductionRegressionTest`, `QuoteFlowE2eTest` |
| Filtros sem efeito e pedidos concluídos na fila ativa | Filtros reais de data e conclusão | `ProductionRegressionTest` |
| Aprovação ou conclusão duplicando pedidos/vendas | Operações idempotentes; checklist completo precede a venda | `BruleeRepositoryTest`, `RepositoryPersistenceRegressionTest` |
| Custo real não podia ser informado | Revisão de cada componente e do tempo ativo, preservando o orçamento original | `ProductionRegressionTest`, `QuoteFlowE2eTest` |
| Receita, ingredientes e cliente fictícios no wizard | Formulário vazio, validação de rendimento/ingredientes, receitas reutilizáveis e revisões persistentes | `WizardRegressionTest`, `RepositoryPersistenceRegressionTest` |
| Formulário perdia informações ao recriar a Activity | Estado restaurável, inclusive ingredientes, unidades e tempos | `WizardRegressionTest` |
| Campos numéricos reformatavam o texto a cada tecla | Buffers de edição separados do cálculo, limites válidos e aviso de preço abaixo do custo | `WizardRegressionTest`, `PriceScenarioIndividualE2eTest` |
| Orçamentos salvos sem acesso posterior | Tela Pedidos com filtros, compartilhamento, aprovação e recusa | `OperationalScreensRegressionTest`, `QuoteFlowE2eTest` |
| OCR descartava preços de ingredientes novos | Cadastro do ingrediente antes da observação; estoque manual, PDF/scanner e lista de compras acessíveis | `OcrReviewE2eTest`, `OperationalScreensRegressionTest` |
| Custos fixos, margem padrão e exportação JSON inacessíveis | Controles reais nas configurações | `OperationalScreensRegressionTest` |
| Painel financeiro quebrava sem vendas | Gráfico construído apenas com séries não vazias | `FinanceRegressionTest` |
| Ranking repetia vendas e chamava lucro do lote de lucro por unidade | Agregação por receita com lucro acumulado e margem ponderada | `RecipeProfitabilityRankingTest`, `FinanceRegressionTest` |
| Backup podia ignorar gravações no WAL | Cursor do checkpoint efetivamente consumido antes da cópia | `BackupRestoreRegressionTest` |
| Restauração fechava o Room ainda usado pelo app e substituía um DataStore ativo | Validação em cópia temporária, restauração transacional no banco ativo e atualização pelo DataStore | `BackupRestoreRegressionTest` |
| Interação visual estática | Transições, progresso animado, resposta ao toque e mascote com animações finitas; movimento reduzido respeitado | `WizardRegressionTest`, `ProductionRegressionTest` |
| Dados dos testes contaminavam o app | Fixtures apenas em fontes de teste; preparação e teardown por caso, espera das gravações, limpeza de preferências e arquivos temporários | Teardown de todos os testes E2E, testes Room em memória e `TemporaryFolder` nos testes de documentos |

## Validação

Os resultados consolidados e os comandos executados ficam em `validation-summary.json` e nos XMLs deste diretório. A suíte completa foi executada primeiro; as falhas identificadas foram corrigidas e os casos afetados executados novamente. A consolidação mantém o resultado mais recente de cada caso e identifica o arquivo de origem, sem apresentar as rodadas como uma única execução.

Dispositivo: AVD visível `Medium_Phone`, imagem API 37.2. Tempos do emulador não representam uma medição de desempenho de aparelho físico. Os testes de WhatsApp interceptam o Intent; nenhuma mensagem real é enviada.

Ao terminar a validação, o APK de desenvolvimento é instalado, os dados são limpos e o app é aberto na bancada vazia. Nenhuma fixture é incluída no APK do app.

Resultado final: **280 casos de UI verificados, sem falhas pendentes**, **2 testes de persistência/migração** (incluindo 20 ciclos isolados), **7 testes de OCR/PDF** e **75 testes unitários únicos** aprovados. Lint, build debug e build release aprovados.

APK: `brulee-debug.apk`. SHA-256: `8a0767a55debcd3cb691522342654dfc3c44c4f6d7ab2122ad1e4cc597853bd7`.
