# Revisão de UX e regressões — Brûlée Telie 0.2.0

> Entrega anterior. A implementação posterior de `fix.md` e seu APK estão em [../fix/REVIEW.md](../fix/REVIEW.md).

Referência visual: `../../layout.png`. Esta revisão substitui as conclusões anteriores sobre a apresentação do PDF, gráfico com uma única venda e adequação das métricas de jogo.

| Relato | Alteração | Regressão |
| --- | --- | --- |
| Calcular custo total permanece desabilitado | Campos operacionais opcionais vazios valem zero e apagam o valor anterior do cálculo. Valores inválidos ou negativos indicam o campo a corrigir. Vírgula decimal e espaços são aceitos. | `WizardRegressionTest` |
| PDF com poucos dados e campos brutos | Documento A4 com identificação, cliente, telefone, produto, descrição, quantidade, valor médio unitário, entrega, validade, observações e total. Modelo interno contém os cinco componentes de custo, lucro e margem, em reais. | `DocumentsModuleTests`, `PdfLayoutRegressionTest`, `OperationalScreensRegressionTest` |
| XP, níveis e ofensivas inadequados | Conclusão apresenta custos previstos/reais, venda, lucro e margem. Animações e resposta ao toque preservadas; sons de conquista removidos da interface. | `CompletionRegressionTest` |
| Ícones e botões desproporcionais | Família de ícones vetoriais com traço uniforme, botões de 54/50 dp com cantos de 18 dp, cinco abas com ícones de 24 dp e rótulos alinhados. | `NavigationLayoutRegressionTest`, `AccessibilityE2eTest` |
| Gráfico branco após venda de R$ 91,11 | Barras explícitas de receita e lucro por venda, inclusive primeira venda, prejuízo e mudança para período sem dados. O teste verifica pixels desenhados e proporção das barras, além dos valores. | `FinanceRegressionTest` |
| Top bar e lista de compras | Marca com monograma B, ação de configurações, navegação principal sem compras. Dados legados de compras permanecem compatíveis com backups. | `NavigationLayoutRegressionTest`, `BackupRestoreRegressionTest` |
| Cadastros fictícios após instalação | Fixtures apenas no APK de testes, preparação e limpeza por caso, verificação direta de todas as tabelas e preferências, backup/restauração automáticos desativados no debug. Verificação do conteúdo do APK também incluída no CI. | `NavigationLayoutRegressionTest`, teardown E2E, `scripts/check-apk-data.py` |

## Dados e instalação

Instalar uma atualização Android preserva os dados locais existentes. O APK não é um banco de dados e não apaga registros do aparelho durante uma atualização. Para a instalação de revisão com base vazia, use:

```bash
./scripts/install-clean-debug.sh
```

O comando instala o APK de `artifacts/functional-review/brulee-debug.apk`, limpa os dados desse aplicativo e abre a bancada. O backup manual em ZIP continua disponível. A preferência de restauração automática foi desabilitada somente na distribuição debug.

Os testes criam apenas suas próprias fixtures e as excluem, inclusive em falhas. PDFs e imagens deste diretório são evidências exportadas para revisão; não são dados do app e não entram no APK.

## Validação

AVD `Medium_Phone`, imagem Android API 37.2, com janela visível. O envio pelo WhatsApp é verificado por interceptação de Intent; os testes não enviam mensagens reais.

Os XMLs e os comandos estão em `results/`; a consolidação está em [validation-summary.json](validation-summary.json).

- 264 casos únicos de Compose verificados no API 37.2, sem falhas pendentes. A rodada ampla encontrou duas expectativas antigas (margem fora da área visível e rótulo incorreto de lucro por unidade). Foram corrigidas; a rodada final de 16 casos, incluindo ambos e o fluxo completo, passou. As rodadas são registradas separadamente.
- 75 testes unitários únicos aprovados, além de 3 testes da verificação de distribuição.
- 2 testes de persistência/migração e 10 testes de documentos aprovados. Os 3 casos de layout/paginação/privacidade do PDF passaram novamente após o ajuste da anotação de SDK solicitado pelo lint.
- Builds debug/release e lint aprovados.
- Instalação final auditada: **zero registros nas 21 tabelas de negócio**, versão instalada **0.2.0 / código 2**, sem backup automático e sem pacotes de teste restantes.

APK: [brulee-0.2.0-debug.apk](brulee-0.2.0-debug.apk). Os caminhos antigos em `functional-review/` e `final-apk/` também foram atualizados com os mesmos bytes, para evitar instalar uma entrega anterior por engano.

SHA-256: `a5da0630a061a7cd901c54fc002ce08dafe437f9daf13c31a2fdfebe390eab80`.

Evidências: [bancada vazia](clean-start.png), [PDF renderizado](customer-preview.png), [PDF exportado](customer-preview.pdf) e [contagem das tabelas](clean-database.json).
