# Brûlée Telie

Aplicativo Android local-first para custos, precificação, orçamentos, produção e análise financeira de confeitaria artesanal.

## Estado atual

Esta fundação implementa o plano técnico e as camadas de dados e documentos:

- projeto multi-módulo (`app`, `domain`, `data`, `documents`);
- banco de dados Room SQLite (`BruleeDatabase`, DAOs, conversores, mappers imutáveis);
- armazenamento de configurações operacionais via DataStore Preferences (`OperatingSettingsDataStore`);
- repositório unificado e reativo (`BruleeRepository`) conectado ao Room e DataStore;
- geradores de PDF (`DefaultPdfGenerator` para modelo limpo de Cliente e relatório detalhado Interno com auditoria de custos);
- parser de cupom fiscal/OCR e casamento de ingredientes por alias (`ReceiptParser` & `IngredientMatcher`);
- gerenciador de backup e restauração zip portátil com checksum SHA-256 (`BackupManager`);
- design system Material 3 baseado na especificação Stitch;
- shell operacional com Produção, Finanças, Pedidos e Estoque;
- tela interativa de fila de produção;
- dashboard financeiro inicial;
- `Money` em centavos e motor de precificação Pure Kotlin;
- suíte completa de testes unitários (`:domain`, `:data`, `:documents`, `:app`).

## Solução de Erro de JVM do Gradle ("Incompatible Gradle JVM version")

Se o Android Studio exibir o aviso:
> *Incompatible Gradle JVM version. The project's Gradle version 8.12 is incompatible with the Gradle JVM version 25 currently selected to run Gradle build.*

Basta clicar em **"Apply compatible Daemon JVM criteria and sync"** (Aplicar critérios de JVM do Daemon compatíveis e sincronizar) na barra amarela de notificação do Android Studio. O arquivo `gradle/gradle-daemon-jvm.properties` configurado no projeto solicitará automaticamente o uso de JDK 17/21 compatível.

Alternativamente, no Android Studio:
1. Vá em **Settings / Preferences** > **Build, Execution, Deployment** > **Build Tools** > **Gradle**.
2. Em **Gradle JDK**, selecione um JDK 17 ou JDK 21 (como o JBR 17/21 ou Eclipse Temurin 17/21) em vez do Java 25.
3. Clique em **Sync Project with Gradle Files**.

1. **Através do Android Studio (Compose Previews)**:
   - Abra qualquer tela em `app/src/main/kotlin/com/bruleetelie/app/ui/screens/`.
   - Clique em **Split** ou **Design** no Android Studio para visualizar o Compose Preview interativo instantaneamente sem precisar compilar o APK completo.
   - Previews disponíveis:
     - `ProductionBoardScreenPreview`
     - `NewQuoteWizardScreenPreview`
     - `FinanceDashboardScreenPreview`
     - `ProductionCompletionScreenPreview`
     - `OcrReviewScreenPreview`

2. **No Emulador ou Dispositivo Físico**:
   - Selecione o módulo `app` no Android Studio.
   - Execute (`Shift + F10` ou botão `Run`).
   - A aplicação será iniciada com o banco de dados Room e DataStore inicializados, permitindo navegar entre as abas **Produção**, **Novo Orçamento**, **Finanças** e **Estoque/OCR**.

## Próximos incrementos

1. Persistência Room e configurações DataStore.
2. Wizard de receita e custos operacionais.
3. Orçamento, PDF e compartilhamento.
4. Fila de produção ligada ao banco local.
5. Scanner/OCR e revisão humana.
