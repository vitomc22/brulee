# Brûlée Telie

Aplicativo Android local-first para custos, precificação, orçamentos, produção e análise financeira de confeitaria artesanal.

## Estado atual

Esta fundação implementa a primeira fatia vertical do plano técnico:

- projeto multi-módulo (`app`, `domain`, `data`, `documents`);
- design system Material 3 baseado na especificação Stitch;
- shell operacional com Produção, Finanças, Pedidos e Estoque;
- tela interativa de fila de produção;
- dashboard financeiro inicial;
- `Money` em centavos e motor de precificação Pure Kotlin;
- teste unitário do cálculo financeiro.

## Abrir e executar

1. Abra a raiz do projeto no Android Studio.
2. Use JDK 21 (o projeto gera bytecode Java 17) e instale o Android SDK 35.
3. Sincronize o Gradle e execute a configuração `app` em um emulador Android 8+.

O Gradle Wrapper já está incluído. O ambiente de criação não possuía Android SDK; a compilação completa do APK deve ser feita no Android Studio após instalar o SDK 35.

## Próximos incrementos

1. Persistência Room e configurações DataStore.
2. Wizard de receita e custos operacionais.
3. Orçamento, PDF e compartilhamento.
4. Fila de produção ligada ao banco local.
5. Scanner/OCR e revisão humana.
