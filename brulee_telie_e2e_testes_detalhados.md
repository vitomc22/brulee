# Brulee Telie — Casos de Teste E2E Detalhados

# Custo real de produção

## COST-E2E-001 — Custo proporcional de um ingrediente

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Criar orçamento do Bolo Brulee.
4. Informar chocolate 2 kg/R$80.
5. Usar 350 g.
6. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
7. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir custo do chocolate de R$ 14,00.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-002 — Somatório dos ingredientes

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Cadastrar chocolate, creme e açúcar da massa canônica.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir custo de ingredientes de R$ 24,60.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-003 — Custo de mão de obra por tempo ativo

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Informar 45 min de preparo + 30 min de decoração com hora a R$30.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir 75 min e R$ 37,50 de mão de obra.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-004 — Custo de gás por duração

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Informar gás por 30 min com R$4/h.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir R$ 2,00.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-005 — Custo de energia por potência e duração

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Informar forno de 2 kW por 45 min e energia R$1,20/kWh.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir R$ 1,80.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-006 — Custo total previsto completo

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Concluir o wizard usando toda a massa canônica.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir custo total de R$ 70,90.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-007 — Persistência do custo após fechar o app

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.
- O ambiente de teste deve permitir encerrar completamente o processo e iniciar uma nova instância do app.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Criar orçamento calculado.
4. Encerrar processo.
5. Reabrir orçamento.
6. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
7. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
8. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Custo continua R$ 70,90 e componentes permanecem idênticos.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-008 — Snapshot não muda após alterar preço do chocolate

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Criar e aprovar orçamento.
4. Depois alterar chocolate de R$80 para R$100/2kg.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Orçamento aprovado mantém R$ 70,90.
- nova receita usa novo preço.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## COST-E2E-009 — Nova receita usa preço atualizado

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Após alterar chocolate para R$100/2kg, criar novo orçamento idêntico.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Novo custo reflete R$0,05/g e não reutiliza snapshot antigo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## COST-E2E-010 — Quantidade zero usada

**Prioridade:** P0  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Informar ingrediente comprado normalmente e 0 g usados.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Custo desse ingrediente é R$0,00 sem alterar preço de compra.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-011 — Unidade kg para g

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Comprar 2 kg e usar 350 g.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Conversão é automática e custo proporcional correto.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-012 — Unidade g para kg

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Cadastrar compra em g e uso em kg equivalente.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Resultado financeiro é igual ao equivalente normalizado.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-013 — Unidade L para ml

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Cadastrar líquido em litros e uso em ml.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Cálculo utiliza unidade normalizada correta.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-014 — Conversão incompatível bloqueada

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Cadastrar ingrediente em kg e tentar informar uso em litros sem densidade.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- UI bloqueia cálculo e pede unidade compatível/densidade.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-015 — Preço com centavos não perde precisão

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Comprar 1 kg por R$10,01 e usar 333 g.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Valor exibido corresponde ao cálculo decimal arredondado, sem artefato de Double.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-016 — Ingrediente extremamente barato

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Comprar 10 kg por R$1,00 e usar 1 g.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Sistema não gera valor negativo/não-numérico e aplica política de arredondamento.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-017 — Ingrediente caro

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Cadastrar insumo de R$9.999,99 e uso integral.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Custo total preserva valor sem overflow ou truncamento.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-018 — Múltiplos ingredientes iguais em versões diferentes

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Criar receita v1.
4. Criar v2 alterando só quantidade de chocolate.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Cada versão mantém seu próprio custo e pode ser aberta pelo histórico.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## COST-E2E-019 — Embalagem entra no custo total

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Alterar embalagem de R$5 para R$8 antes do orçamento.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Custo total aumenta exatamente R$3.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-020 — Outros custos entram no custo

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Adicionar custo manual R$7,35.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Total aumenta exatamente R$7,35 e item aparece no detalhamento.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-021 — Tempo passivo não vira mão de obra

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Informar forno 3h e trabalho ativo 30min.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Mão de obra considera 30min.
- energia considera 3h.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-022 — Receita sem gás

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Selecionar apenas forno elétrico.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Gás = R$0,00.
- custo elétrico calculado normalmente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-023 — Receita sem energia

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Selecionar apenas fogão a gás.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Energia = R$0,00.
- gás calculado normalmente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-024 — Receita usa gás e energia

**Prioridade:** P1  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Selecionar ambos e informar tempos independentes.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Os dois custos aparecem separadamente e são somados uma única vez.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-025 — Voltar no wizard e alterar quantidade

**Prioridade:** P2  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Calcular chocolate com 350g.
4. Voltar e trocar para 400g.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Resumo atualiza somente a diferença do chocolate.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-026 — Cancelar edição restaura cálculo anterior

**Prioridade:** P2  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android, Persistência/Backup

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Abrir edição de ingrediente.
4. Alterar valor.
5. Cancelar.
6. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
7. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
8. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Resumo mantém valor persistido anterior.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A restauração não pode produzir perda silenciosa nem duplicação de registros.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-027 — Excluir ingrediente recalcula total

**Prioridade:** P2  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Remover açúcar do orçamento antes da aprovação.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Total diminui R$0,60.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-028 — Duplicar ingrediente exige decisão

**Prioridade:** P2  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Adicionar chocolate duas vezes na mesma receita.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- UI evita duplicidade acidental ou mantém linhas explícitas.
- total não duplica silenciosamente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-029 — Estado sem dados suficientes

**Prioridade:** P2  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Criar receita sem preço de um ingrediente.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- App informa que não pode determinar custo preciso e não inventa R$0.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## COST-E2E-030 — Offline completo no cálculo

**Prioridade:** P2  
**Objetivo validado:** Determinar com precisão o custo de produção da receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Moeda configurada como BRL e locale pt-BR.
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o próprio caso determinar outro valor.
- Gás configurado em R$ 4,00/h, salvo quando o próprio caso determinar outro valor.
- Energia configurada em R$ 1,20/kWh e forno em 2,0 kW quando o cenário utilizar energia elétrica.
- Desabilitar Wi‑Fi e dados móveis antes do primeiro passo funcional.

### Massa de dados
- Chocolate: compra de 2.000 g por R$ 80,00; uso de 350 g → R$ 14,00.
- Creme: compra de 1.000 g por R$ 25,00; uso de 400 g → R$ 10,00.
- Açúcar: compra de 1.000 g por R$ 6,00; uso de 100 g → R$ 0,60.
- Embalagem padrão: R$ 5,00.
- Preparo ativo: 45 min; decoração ativa: 30 min; trabalho ativo total: 75 min.
- Gás: 30 min; forno elétrico: 45 min.
- Custo total canônico da receita: R$ 70,90.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Desabilitar rede.
4. Criar receita e concluir cálculo.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Custo é calculado e persistido localmente sem erro de rede.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum erro de conectividade deve impedir a conclusão do fluxo local.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- O app exige conexão para uma função que deveria ser local.

---

# Preço de venda recomendado

## PRICE-E2E-001 — Preço sugerido para margem de 40%

**Prioridade:** P0  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Concluir custo de R$70,90 com margem alvo 40%.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir R$118,17.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-002 — Lucro previsto acompanha preço sugerido

**Prioridade:** P0  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Aceitar R$118,17.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir lucro previsto de R$47,27.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-003 — Margem exibida é margem e não markup

**Prioridade:** P0  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Aceitar preço sugerido.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir ~40,00%, não 66,67%.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-004 — Alteração manual de preço recalcula lucro

**Prioridade:** P0  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Alterar venda de R$118,17 para R$130,00.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Lucro e margem são atualizados imediatamente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-005 — Preço abaixo do custo

**Prioridade:** P0  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Definir preço de venda R$60 para custo R$70,90.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Mostrar lucro negativo e alerta de venda abaixo do custo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-006 — Margem alvo 0%

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Configurar margem 0 e custo positivo.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Preço sugerido = custo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-007 — Margem alvo 10%

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Configurar 10% para custo R$70,90.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Preço = custo/(0,9), arredondado conforme política.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-008 — Margem alvo 50%

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Configurar 50%.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Preço sugerido = R$141,80.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-009 — Margem alvo muito alta

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Configurar margem próxima de 100%.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- UI valida limite e evita divisão inválida/infinito.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-010 — Margem negativa bloqueada ou explicitamente tratada

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Tentar margem menor que 0.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Sistema rejeita valor inválido ou comunica desconto/prejuízo explicitamente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-011 — Custo zero

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Criar receita de custo zero e solicitar preço.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Sistema trata caso sem divisão inválida e não exibe NaN.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-012 — Arredondamento de preço sugerido

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Criar custo que gere terceira casa decimal.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Preço final exibe 2 casas com HALF_UP.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-013 — Preço final persiste no orçamento

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.
- O ambiente de teste deve permitir encerrar completamente o processo e iniciar uma nova instância do app.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Alterar preço sugerido manualmente.
4. Salvar rascunho.
5. Reabrir.
6. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
7. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
8. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Preço manual permanece.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-014 — Preço do orçamento aprovado vira snapshot

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Aprovar orçamento e depois alterar margem padrão.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Preço do pedido aprovado não muda.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## PRICE-E2E-015 — Nova margem afeta somente novos orçamentos

**Prioridade:** P1  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Alterar margem padrão após venda antiga.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Novo orçamento usa nova margem.
- histórico não muda.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## PRICE-E2E-016 — Editar custo antes de aprovar recalcula preço

**Prioridade:** P2  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Alterar quantidade de ingrediente no rascunho.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Preço sugerido é recalculado com novo custo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-017 — Editar custo após preço manual

**Prioridade:** P2  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Definir preço manual.
4. Alterar ingrediente.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- App preserva ou sinaliza preço manual segundo regra definida, sem sobrescrever silenciosamente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-018 — Valor grande de venda

**Prioridade:** P2  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Definir preço de R$999.999,99.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Persistir/exibir sem overflow e com formatação pt-BR.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-019 — Campo monetário com vírgula

**Prioridade:** P2  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Digitar 118,17 em pt-BR.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Interpretar como R$118,17.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-020 — Campo monetário não aceita texto inválido

**Prioridade:** P2  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Digitar letras/símbolos indevidos.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Impedir confirmação e manter cálculo íntegro.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## PRICE-E2E-021 — Preço sugerido disponível offline

**Prioridade:** P2  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.
- Desabilitar Wi‑Fi e dados móveis antes do primeiro passo funcional.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Sem rede, concluir orçamento.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Preço é calculado localmente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum erro de conectividade deve impedir a conclusão do fluxo local.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- O app exige conexão para uma função que deveria ser local.

---

## PRICE-E2E-022 — Preço no PDF é igual ao preço do orçamento

**Prioridade:** P2  
**Objetivo validado:** Determinar por quanto a receita deve ser vendida e validar lucro/margem.  
**Plataformas/áreas:** Android, PDF

### Pré-condições
- Banco de teste isolado ou limpo para o cenário.
- Locale pt-BR e moeda BRL.
- Motor de custo funcional e receita com custo persistido antes da etapa de precificação.
- Margem padrão de 40%, salvo quando o caso informar outro valor.

### Massa de dados
- Custo canônico da receita: R$ 70,90.
- Margem alvo padrão: 40%.
- Preço sugerido canônico: R$ 118,17.
- Lucro previsto canônico: R$ 47,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o fluxo de criação/edição do orçamento ou receita alvo.
3. Gerar PDF após definir preço.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- PDF mostra exatamente o preço persistido.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo PDF gerado e o trecho visual onde o valor testado aparece.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

# Lucro, margem e faturamento

## EARN-E2E-001 — Lucro real após venda sem alterações

**Prioridade:** P0  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir venda a R$118,17 com custo real R$70,90.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Lucro real = R$47,27.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-002 — Lucro real após aumento de custo

**Prioridade:** P0  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir execução real da massa de desvio positivo.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Lucro real = R$34,80.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-003 — Margem real após aumento de custo

**Prioridade:** P0  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Usar venda R$118,17 e custo R$83,37.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Margem real = 29,45%.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-004 — Lucro real após economia

**Prioridade:** P0  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Finalizar execução com custo R$65,90.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Lucro = R$52,27.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-005 — Financeiro usa custo real, não previsto

**Prioridade:** P0  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir pedido com previsto R$70,90 e real R$83,37.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Dashboard usa R$83,37 para lucro.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-006 — Venda histórica não muda após atualizar insumos

**Prioridade:** P0  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir venda.
4. Depois alterar todos os preços de compra.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Lucro histórico permanece idêntico.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## EARN-E2E-007 — Pagamento pendente separado de lucro operacional

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir produção sem marcar pagamento.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Venda pode ter lucro calculado, mas status financeiro continua pendente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-008 — Pagamento parcial

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Registrar pagamento parcial.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Status = PARTIAL.
- receita recebida e saldo são coerentes.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-009 — Pagamento total

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Completar pagamento.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Status = PAID e saldo = R$0,00.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-010 — Reembolso

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Marcar pagamento como reembolsado.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Financeiro reflete regra de receita líquida definida e mantém trilha histórica.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## EARN-E2E-011 — Venda cancelada não entra como faturamento concluído

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Cancelar pedido antes de conclusão.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Dashboard não soma como venda concluída.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-012 — Múltiplas vendas somam faturamento

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir 3 vendas com valores conhecidos.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Faturamento é soma exata.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-013 — Múltiplas vendas somam lucro real

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir receitas de ranking.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Lucro agregado = soma dos lucros reais.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## EARN-E2E-014 — Ticket médio

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir vendas de R$180, R$100 e R$75.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ticket médio = R$118,33 após arredondamento.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-015 — Lucro negativo

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Venda R$60 com custo real R$70,90.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Lucro = -R$10,90 e dashboard evidencia prejuízo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-016 — Custo real zero

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir venda gratuita de custo zero ou amostra de teste.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Não ocorrer divisão inválida.
- margem segue regra explícita.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-017 — Receita zero com custo positivo

**Prioridade:** P1  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Registrar perda/descarte sem receita.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Financeiro mostra custo/perda sem NaN.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-018 — Filtro mensal

**Prioridade:** P2  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Criar vendas em meses diferentes.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Resumo do mês inclui apenas período selecionado.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-019 — Virada de ano

**Prioridade:** P2  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Criar vendas em dez/2026 e jan/2027.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Filtros não misturam anos.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-020 — Fuso horário na virada do dia

**Prioridade:** P2  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.
- Fixar relógio e timezone do ambiente de teste conforme o cenário.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir venda próxima de 00:00 local.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Venda aparece no dia/mês local correto.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-021 — Reabrir app mantém métricas

**Prioridade:** P2  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.
- O ambiente de teste deve permitir encerrar completamente o processo e iniciar uma nova instância do app.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir vendas.
4. Fechar/reabrir.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- KPIs permanecem iguais.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-022 — Exportar e importar backup preserva lucro

**Prioridade:** P2  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android, Persistência/Backup

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Exportar dataset.
4. Restaurar em instalação limpa.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Lucros e margens são idênticos.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A restauração não pode produzir perda silenciosa nem duplicação de registros.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-023 — PDF interno usa lucro real após conclusão

**Prioridade:** P2  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android, PDF

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir produção com desvio e gerar relatório interno.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Documento mostra custo/lucro real, não estimado.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo PDF gerado e o trecho visual onde o valor testado aparece.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## EARN-E2E-024 — Valores insuficientes não aparecem como zero

**Prioridade:** P2  
**Objetivo validado:** Determinar quanto foi efetivamente ganho após a venda.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com a venda necessária ao cenário.
- Locale pt-BR e moeda BRL.
- Orçamento e produção possuem snapshots próprios.
- Financeiro utiliza custo real depois da conclusão da produção.

### Massa de dados
- Preço de venda canônico: R$ 118,17.
- Custo previsto canônico: R$ 70,90.
- Custo real com estouro: R$ 83,37.
- Lucro real com estouro: R$ 34,80.
- Margem real com estouro: 29,45%.
- Custo real com economia: R$ 65,90.
- Lucro real com economia: R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Sem vendas no período.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- UI mostra estado sem dados ou R$0 de forma semanticamente correta, sem inventar margem.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

# Receitas mais lucrativas

## RANK-E2E-001 — Ranking por lucro absoluto

**Prioridade:** P0  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Cadastrar/concluir Bolo, Brigadeiro, Trufa e Beijinho da massa.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ordem: Bolo (80), Brigadeiro (50), Trufa/Beijinho (45), com desempate definido.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-002 — Ranking por margem

**Prioridade:** P0  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Usar massa de ranking.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ordem inicia por Trufa 60%, depois Brigadeiro/Beijinho 50%, Bolo 44,44%.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-003 — Ranking por lucro/hora

**Prioridade:** P0  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Usar massa de ranking.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ordem: Trufa 90/h, Beijinho 60/h, Brigadeiro 50/h, Bolo 20/h.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-004 — Ranking usa custo real

**Prioridade:** P0  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Criar receita cujo real diverge do previsto.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ranking é baseado no critério real quando venda está concluída.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-005 — Venda antiga mantém posição com snapshot

**Prioridade:** P0  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Alterar preço atual dos insumos após vendas concluídas.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Resultados históricos do ranking não são recalculados retroativamente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-006 — Receita sem venda não entra no ranking de vendas

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Criar receita sem venda concluída.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Não aparece como rentabilidade observada.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-007 — Pedido cancelado não entra

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Criar e cancelar pedido.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Não contribui para receita/lucro de ranking.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-008 — Empate de lucro

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Trufa e Beijinho têm lucro R$45.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Empate é tratado de forma determinística e estável.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-009 — Empate de margem

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Brigadeiro e Beijinho têm 50%.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ordenação é estável e não muda aleatoriamente entre aberturas.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-010 — Receita com prejuízo aparece no fim

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Adicionar receita com lucro negativo.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Fica abaixo de receitas positivas no ranking por lucro.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-011 — Ranking por período

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Selecionar mês em que só duas receitas venderam.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- A lista contém apenas dados do período.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-012 — Ranking anual

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Selecionar ano completo.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Agregação soma todas as vendas válidas do ano.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-013 — Ranking por lucro médio

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Vender mesma receita várias vezes com custos reais diferentes.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Métrica média é calculada a partir das vendas selecionadas.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-014 — Ranking por quantidade vendida

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Criar volumes diferentes.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ordenação corresponde à quantidade real vendida.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-015 — Ranking por faturamento

**Prioridade:** P1  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Criar receitas com faturamentos distintos.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ordenação usa revenue agregado, não lucro.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-016 — Amostra exibida

**Prioridade:** P2  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Receita com apenas 1 venda.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- UI indica quantidade de vendas da amostra.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-017 — Mudança de filtro atualiza todos os indicadores

**Prioridade:** P2  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Alternar lucro/margem/lucro-hora.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Valores e ordem mudam juntos, sem resíduos do filtro anterior.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-018 — Persistência de filtro é coerente

**Prioridade:** P2  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Selecionar ranking por margem.
4. Navegar e voltar.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Filtro permanece ou volta ao padrão conforme especificação, sem estado inconsistente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-019 — Web e Android exibem mesmo ranking após importação

**Prioridade:** P2  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android, Web

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.
- Disponibilizar a interface web com um ambiente de dados vazio para importação do mesmo conjunto usado no Android.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Exportar dados Android e importar na análise web.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Mesma ordem e mesmos valores para igual filtro.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Os valores exibidos na web devem coincidir centavo a centavo com a origem quando a mesma regra de agregação for aplicada.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Screenshot Android e screenshot Web do mesmo período/filtro.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-020 — Receita renomeada preserva histórico

**Prioridade:** P2  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Concluir vendas.
4. Renomear receita.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Histórico/ranking mantém dados associados e mostra nome atual ou snapshot conforme regra definida.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-021 — Versões da mesma receita agregadas corretamente

**Prioridade:** P2  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Vender v1 e v2 da mesma receita.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ranking por receita agrega ou separa conforme opção selecionada, sem duplicação.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## RANK-E2E-022 — Dataset grande mantém ordenação correta

**Prioridade:** P2  
**Objetivo validado:** Identificar corretamente quais receitas são mais rentáveis.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com vendas concluídas determinísticas.
- Ranking calculado sobre vendas válidas do período selecionado.
- Pedidos cancelados e rascunhos não contam como venda concluída.
- Pré-carregar dataset determinístico em volume suficiente para estresse sem alterar os valores esperados do cenário.

### Massa de dados
- Bolo: venda R$ 180,00; custo R$ 100,00; lucro R$ 80,00; margem 44,44%; trabalho 4h; lucro/h R$ 20,00.
- Brigadeiro: venda R$ 100,00; custo R$ 50,00; lucro R$ 50,00; margem 50,00%; trabalho 1h; lucro/h R$ 50,00.
- Trufa: venda R$ 75,00; custo R$ 30,00; lucro R$ 45,00; margem 60,00%; trabalho 0,5h; lucro/h R$ 90,00.
- Beijinho: venda R$ 90,00; custo R$ 45,00; lucro R$ 45,00; margem 50,00%; trabalho 0,75h; lucro/h R$ 60,00.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Garantir que todas as vendas necessárias ao ranking estejam concluídas e persistidas.
3. Popular centenas de vendas.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Top N continua matematicamente correto e UI permanece utilizável.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

# Evolução de preço dos ingredientes

## TREND-E2E-001 — Primeira compra cria baseline

**Prioridade:** P0  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Cadastrar chocolate a R$30/kg.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Histórico mostra primeiro ponto sem afirmar alta/queda anterior.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## TREND-E2E-002 — Alta exata de 10% aciona alerta

**Prioridade:** P0  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Cadastrar R$30/kg e depois R$33/kg com threshold 10%.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Alerta de +10,00%.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-003 — Alta abaixo do threshold não aciona

**Prioridade:** P0  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. R$33/kg.
4. R$36/kg representa +9,09%.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Nenhum alerta de threshold 10% para comparação anterior.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-004 — Alta acumulada pode ser exibida separadamente

**Prioridade:** P0  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Comparar Jan R$30 com Mar R$36.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Variação acumulada = +20,00% quando período Jan→Mar é selecionado.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-005 — Queda exata de 10%

**Prioridade:** P0  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. R$36/kg.
4. R$32,40/kg.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Mostrar -10,00% e acionar alerta de variação se regra for absoluta.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-006 — Normalização entre kg e g

**Prioridade:** P0  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Cadastrar uma compra em kg e outra equivalente em g.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Preço normalizado é comparável sem falso alerta.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-007 — Histórico antigo não muda ao editar preço atual

**Prioridade:** P0  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Adicionar nova compra.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Pontos anteriores permanecem imutáveis.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## TREND-E2E-008 — Mesmo ingrediente, fornecedores diferentes

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Cadastrar preços de dois fornecedores.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Histórico geral e filtro por fornecedor mostram valores corretos.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## TREND-E2E-009 — Compra com quantidade fracionada

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Comprar 1,5 kg por preço conhecido.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Preço/kg normalizado é correto.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-010 — Unidade incompatível não entra na mesma série

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Tentar comparar unidade discreta com massa sem conversão.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Sistema exige mapeamento adequado e evita tendência falsa.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-011 — OCR confirmado cria PriceObservation

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android, OCR

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.
- Usar fixture de imagem/PDF determinística e legível; nenhum dado deve ser persistido antes da confirmação quando o cenário assim exigir.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Escanear nota, revisar e confirmar chocolate.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Novo ponto aparece no histórico.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Fixture utilizada e tela de revisão do OCR antes da confirmação.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## TREND-E2E-012 — OCR não confirmado não altera tendência

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android, OCR

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.
- Usar fixture de imagem/PDF determinística e legível; nenhum dado deve ser persistido antes da confirmação quando o cenário assim exigir.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Escanear nota e cancelar revisão.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Nenhum ponto é criado.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Fixture utilizada e tela de revisão do OCR antes da confirmação.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-013 — Nota duplicada não duplica histórico

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android, OCR

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.
- Usar fixture de imagem/PDF determinística e legível; nenhum dado deve ser persistido antes da confirmação quando o cenário assim exigir.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Importar a mesma nota duas vezes.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Segundo fluxo é bloqueado/sinalizado.
- tendência não duplica.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Fixture utilizada e tela de revisão do OCR antes da confirmação.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## TREND-E2E-014 — Correção manual após OCR

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android, OCR

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.
- Usar fixture de imagem/PDF determinística e legível; nenhum dado deve ser persistido antes da confirmação quando o cenário assim exigir.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. OCR lê R$39 mas usuário corrige para R$36.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Histórico usa R$36.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Fixture utilizada e tela de revisão do OCR antes da confirmação.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## TREND-E2E-015 — Compra com desconto

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Registrar preço final efetivamente pago.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Trend usa o valor final definido pela regra do produto.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-016 — Preço igual ao anterior

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Cadastrar duas compras a R$30/kg.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Variação = 0,00%, sem alerta.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-017 — Aumento muito alto

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Preço dobra de R$30 para R$60/kg.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Mostrar +100,00% sem overflow.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-018 — Preço reduz à metade

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. R$60.
4. R$30/kg.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Mostrar -50,00%.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-019 — Preço zero inválido

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Tentar cadastrar compra comercial com preço zero quando não permitido.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Sistema valida e não gera divisão enganosa.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-020 — Quantidade zero inválida

**Prioridade:** P1  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Tentar compra com quantidade 0.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Bloquear normalização/divisão por zero.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-021 — Filtro de período

**Prioridade:** P2  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Selecionar Fev.
4. Abr.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Gráfico e variações usam somente o intervalo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-022 — Ordenação cronológica

**Prioridade:** P2  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Inserir compras fora de ordem temporal.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Gráfico ordena por data, não por ordem de cadastro.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-023 — Duas compras no mesmo dia

**Prioridade:** P2  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Cadastrar dois preços.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ambos permanecem auditáveis e agregação segue regra definida.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-024 — Persistência após reinício

**Prioridade:** P2  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.
- O ambiente de teste deve permitir encerrar completamente o processo e iniciar uma nova instância do app.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Criar histórico.
4. Reiniciar app.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Série e alertas permanecem.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## TREND-E2E-025 — Backup preserva histórico

**Prioridade:** P2  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android, Persistência/Backup

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Exportar/importar backup.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Todos os PriceObservations permanecem idênticos.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- A restauração não pode produzir perda silenciosa nem duplicação de registros.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## TREND-E2E-026 — Web reproduz série importada

**Prioridade:** P2  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android, Web, Persistência/Backup

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.
- Disponibilizar a interface web com um ambiente de dados vazio para importação do mesmo conjunto usado no Android.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Importar backup na web.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Pontos, valores normalizados e percentuais coincidem.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Os valores exibidos na web devem coincidir centavo a centavo com a origem quando a mesma regra de agregação for aplicada.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Screenshot Android e screenshot Web do mesmo período/filtro.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## TREND-E2E-027 — Ingrediente sem histórico suficiente

**Prioridade:** P2  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Apenas uma observação.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- UI mostra dados insuficientes para tendência.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## TREND-E2E-028 — Média móvel não usa dados futuros

**Prioridade:** P2  
**Objetivo validado:** Identificar ingredientes cujo preço está aumentando ou diminuindo.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou preparado com histórico de compras do ingrediente.
- Comparações usam preço normalizado por unidade.
- Threshold de alerta configurado em 10%, salvo quando o cenário informar outro.

### Massa de dados
- Chocolate Jan: R$ 30,00/kg.
- Chocolate Fev: R$ 33,00/kg.
- Chocolate Mar: R$ 36,00/kg.
- Chocolate Abr: R$ 32,40/kg.
- Threshold padrão: 10%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o cadastro/histórico do ingrediente alvo ou criar os registros necessários.
3. Navegar por períodos históricos.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Cálculo de cada ponto usa somente observações aplicáveis ao período.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

# Desvio entre orçamento e custo real

## DEV-E2E-001 — Sem alteração: desvio zero

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Aprovar orçamento R$70,90 e finalizar sem mudanças.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Real R$70,90.
- desvio R$0,00 e 0,00%.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-002 — Desvio positivo completo

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Aplicar massa real de R$83,37.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir +R$12,47 e +17,59%.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-003 — Desvio negativo completo

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Aplicar massa real de R$65,90.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir -R$5,00 e -7,05%.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-004 — Desvio de ingrediente

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Alterar chocolate 350.
4. 420g.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Diferença isolada do item = +R$2,80.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-005 — Desvio de mão de obra

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Decoração 30.
4. 45min.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Diferença de mão de obra = +R$7,50.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-006 — Desvio de gás

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Gás 30.
4. 40min.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Diferença monetária arredondada = +R$0,67.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-007 — Desvio de embalagem

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Embalagem R$5.
4. R$6,50.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Diferença = +R$1,50.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-008 — Lucro recalcula com custo real

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Finalizar desvio positivo mantendo preço R$118,17.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Lucro real = R$34,80.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-009 — Margem recalcula com custo real

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Mesmo cenário.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Margem real = 29,45%.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-010 — Quote snapshot permanece R$70,90

**Prioridade:** P0  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Depois de finalizar real R$83,37, abrir orçamento original.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Estimado continua R$70,90.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## DEV-E2E-011 — Editar somente um campo

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Na finalização marcar alterações e mudar apenas embalagem.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Demais valores reais assumem previstos.
- só embalagem diverge.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-012 — Cancelar revisão de mudanças

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Abrir alteração e cancelar.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Pedido continua não finalizado e valores reais não são gravados.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-013 — Finalização atômica

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.
- O ambiente de teste deve permitir encerrar completamente o processo e iniciar uma nova instância do app.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Forçar fechamento do app durante confirmação final.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Após reabrir, estado é anterior ou totalmente concluído, nunca parcial inconsistente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-014 — Desvio percentual com custo previsto pequeno

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Usar custo previsto baixo e diferença conhecida.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Percentual é correto e não sofre divisão truncada.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-015 — Custo previsto zero

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Caso permitido, finalizar com custo real >0.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- UI trata percentual como não aplicável/infinito segundo regra explícita, nunca NaN.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-016 — Custo real igual previsto após edições compensatórias

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Aumentar um item e reduzir outro pelo mesmo valor.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Desvio total = 0, mas detalhamento mostra diferenças por categoria.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-017 — Várias alterações no mesmo ingrediente

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Editar quantidade mais de uma vez antes de concluir.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Somente valor final entra no custo real.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-018 — Alteração de tempo passivo não muda mão de obra

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Aumentar forno em 30min sem mudar trabalho ativo.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Energia muda.
- mão de obra não.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-019 — Alteração de tempo ativo muda mão de obra

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Aumentar decoração ativa.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Custo de trabalho aumenta exatamente pelo tempo × hora.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-020 — Desvio refletido no financeiro mensal

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Finalizar pedido com desvio.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Dashboard usa custo real e lucro real.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-021 — Desvio refletido no ranking

**Prioridade:** P1  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Finalizar receita com piora de custo.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Ranking por lucro usa novo lucro real.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## DEV-E2E-022 — Filtro previsto versus real por período

**Prioridade:** P2  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Criar pedidos em meses diferentes.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Gráfico inclui apenas período selecionado.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-023 — Venda ainda em produção não entra como custo real final

**Prioridade:** P2  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Pedido aprovado mas não finalizado.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Relatório diferencia estimativa de realizado.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-024 — Reabrir venda concluída é somente leitura ou auditada

**Prioridade:** P2  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.
- O ambiente de teste deve permitir encerrar completamente o processo e iniciar uma nova instância do app.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Tentar editar real após conclusão.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Sistema bloqueia ou cria ajuste auditável.
- não altera silenciosamente histórico.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## DEV-E2E-025 — Backup preserva previsto e real

**Prioridade:** P2  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android, Persistência/Backup

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Exportar/importar venda com desvio.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Ambos snapshots e diferença permanecem.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- A restauração não pode produzir perda silenciosa nem duplicação de registros.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## DEV-E2E-026 — Web mostra mesma diferença

**Prioridade:** P2  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android, Web

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.
- Disponibilizar a interface web com um ambiente de dados vazio para importação do mesmo conjunto usado no Android.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Importar dados na web.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- R$12,47 e 17,59% coincidem.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Os valores exibidos na web devem coincidir centavo a centavo com a origem quando a mesma regra de agregação for aplicada.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Screenshot Android e screenshot Web do mesmo período/filtro.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-027 — Valores negativos de quantidade são bloqueados

**Prioridade:** P2  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Tentar informar -10g como real.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- UI não permite custo inválido.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## DEV-E2E-028 — Formatação de sinal

**Prioridade:** P2  
**Objetivo validado:** Medir com precisão o desvio entre orçamento previsto e execução real.  
**Plataformas/áreas:** Android

### Pré-condições
- Existe orçamento aprovado com snapshot de custo previsto.
- A produção pode registrar valores reais sem sobrescrever o snapshot previsto.
- Preço de venda permanece associado ao orçamento/pedido original.

### Massa de dados
- Custo previsto do orçamento: R$ 70,90.
- Preço de venda: R$ 118,17.
- Execução real de estouro: R$ 83,37.
- Desvio esperado no estouro: +R$ 12,47 / +17,59%.
- Execução real com economia: R$ 65,90.
- Desvio esperado na economia: -R$ 5,00 / -7,05%.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Visualizar economia e estouro.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Economia exibe sinal negativo/estado favorável.
- estouro positivo/alerta de forma consistente.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

# Trabalho investido por receita

## WORK-E2E-001 — Trabalho ativo da receita base

**Prioridade:** P0  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Informar 45min preparo + 30min decoração.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir 75min / 1h15 de trabalho ativo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-002 — Custo de mão de obra da receita base

**Prioridade:** P0  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Hora R$30 e 75min ativos.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir R$37,50.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-003 — Forno passivo não entra no trabalho ativo

**Prioridade:** P0  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Adicionar 45min de forno.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Trabalho permanece 75min.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-004 — Gás passivo não entra no trabalho ativo

**Prioridade:** P0  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Adicionar 30min de cozimento passivo.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Trabalho humano não aumenta.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-005 — Lucro por hora de Bolo

**Prioridade:** P0  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Venda/custo da massa de ranking e 4h ativas.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Exibir R$20,00/h.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## WORK-E2E-006 — Lucro por hora de Brigadeiro

**Prioridade:** P0  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. 50 de lucro / 1h.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- R$50,00/h.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-007 — Lucro por hora de Trufa

**Prioridade:** P0  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. 45 de lucro / 0,5h.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- R$90,00/h.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-008 — Ranking lucro/hora

**Prioridade:** P0  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Usar massa completa.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Trufa > Beijinho > Brigadeiro > Bolo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- A ordenação muda sem alteração de dados ou filtro.

---

## WORK-E2E-009 — Alteração real de tempo ativo

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Previsto 75min.
4. Real 90min.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Histórico mostra previsto e real, e mão de obra real aumenta.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## WORK-E2E-010 — Alteração real somente de forno

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Aumentar forno sem atividade humana.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Horas de trabalho continuam iguais.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-011 — Receita sem decoração

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Informar 0min decoração.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Tempo ativo soma apenas demais etapas.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-012 — Receita longa

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Informar 12h de trabalho ativo.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Sistema exibe/persiste corretamente sem wrap de horário.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-013 — Minutos fracionários ou regra de granularidade

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Tentar tempo fora da granularidade suportada.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- UI normaliza/valida sem erro de cálculo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-014 — Complexidade não altera relógio real

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Mudar Simples.
4. Complexa mantendo tempos.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Horas reais permanecem iguais.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-015 — Complexidade pode alterar custo se multiplicador configurado

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Ativar regra de multiplicador e trocar nível.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Custo de mão de obra muda conforme configuração.
- horas continuam iguais.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-016 — Tempo de montagem separado

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Adicionar montagem ativa.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Entra no activeWorkTime e custo de trabalho.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-017 — Duas sessões de trabalho

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Registrar preparo e depois acabamento em sessão posterior.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Total ativo é soma das sessões.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-018 — Pedido pausado não conta automaticamente

**Prioridade:** P1  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Pausar produção sem atividade.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Tempo não cresce se sistema usa entrada declarada/sessões controladas.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-019 — Relatório por receita

**Prioridade:** P2  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Abrir análise de uma receita com várias vendas.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Mostrar média/total de trabalho conforme filtro.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-020 — Filtro mensal de horas

**Prioridade:** P2  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Criar produções em meses diferentes.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Somente horas do mês selecionado são somadas.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-021 — Backup preserva tempos

**Prioridade:** P2  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android, Persistência/Backup

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Exportar/importar.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Tempos previstos/reais permanecem.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- A restauração não pode produzir perda silenciosa nem duplicação de registros.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-022 — Web reproduz horas e lucro/hora

**Prioridade:** P2  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android, Web

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.
- Disponibilizar a interface web com um ambiente de dados vazio para importação do mesmo conjunto usado no Android.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Importar dados.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Indicadores coincidem com Android.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Os valores exibidos na web devem coincidir centavo a centavo com a origem quando a mesma regra de agregação for aplicada.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Screenshot Android e screenshot Web do mesmo período/filtro.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-023 — Sem tempo ativo suficiente

**Prioridade:** P2  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Venda com tempo não registrado.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Lucro/hora aparece como indisponível, não infinito.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.

---

## WORK-E2E-024 — Alterar valor da hora não reescreve venda antiga

**Prioridade:** P2  
**Objetivo validado:** Medir o trabalho humano investido em cada receita.  
**Plataformas/áreas:** Android

### Pré-condições
- Hora de trabalho configurada em R$ 30,00/h, salvo quando o cenário determinar outro valor.
- O sistema diferencia tempo humano ativo de tempo passivo de equipamento.
- Snapshots de mão de obra são preservados em vendas concluídas.

### Massa de dados
- Hora de trabalho: R$ 30,00/h.
- Preparo ativo canônico: 45 min.
- Decoração ativa canônica: 30 min.
- Trabalho ativo total canônico: 75 min.
- Forno/gás passivos não compõem activeWorkTime.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Abrir o pedido/orçamento preparado para o cenário.
3. Concluir venda a R$30/h.
4. Mudar config para R$40/h.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Venda histórica mantém snapshot de custo de trabalho antigo.
- Nenhum valor financeiro pode exibir NaN, Infinity, truncamento ou artefato de ponto flutuante.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Há diferença de arredondamento fora da política monetária definida.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

# Fluxos ponta a ponta, persistência e resiliência

## CROSS-E2E-001 — Fluxo completo feliz

**Prioridade:** P0  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Criar receita.
4. Custo.
5. Preço.
6. Cliente.
7. Orçamento.
8. Aprovar.
9. Produzir sem mudanças.
10. Concluir.
11. Financeiro.
12. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
13. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Todos os valores da massa canônica permanecem consistentes entre telas.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-002 — Fluxo completo com desvio

**Prioridade:** P0  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Mesmo fluxo, mas alterar execução para R$83,37.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Orçamento preserva previsto.
- venda/financeiro usam real.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-003 — Fluxo completo com economia

**Prioridade:** P0  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Finalizar em R$65,90.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Financeiro mostra melhoria real e histórico preserva os dois valores.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## CROSS-E2E-004 — Rascunho não contamina financeiro

**Prioridade:** P0  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Criar orçamento e salvar como rascunho.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Não entra em faturamento/lucro/ranking.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- A ordenação muda sem alteração de dados ou filtro.

---

## CROSS-E2E-005 — Aprovado ainda não concluído

**Prioridade:** P0  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Aprovar e colocar em produção.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Financeiro distingue pipeline de venda realizada.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-006 — Venda concluída alimenta todos os objetivos

**Prioridade:** P0  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Concluir pedido.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Custo, preço, lucro, ranking, desvio e trabalho ficam consultáveis.
- A ordem exibida deve ser determinística para o mesmo conjunto de dados e mesmo filtro.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- A ordenação muda sem alteração de dados ou filtro.

---

## CROSS-E2E-007 — Reinício no meio do wizard

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- O ambiente de teste deve permitir encerrar completamente o processo e iniciar uma nova instância do app.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Preencher metade da receita.
4. Fechar/reabrir.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Retomar com estado coerente conforme política de draft.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-008 — Reinício no meio da produção

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- O ambiente de teste deve permitir encerrar completamente o processo e iniciar uma nova instância do app.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Começar pedido.
4. Fechar/reabrir.
5. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
6. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
7. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Status e dados persistidos permanecem.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-009 — Operação offline ponta a ponta

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Desabilitar Wi‑Fi e dados móveis antes do primeiro passo funcional.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Sem rede, criar até concluir venda.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Fluxo principal funciona localmente.
- Nenhum erro de conectividade deve impedir a conclusão do fluxo local.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- O app exige conexão para uma função que deveria ser local.

---

## CROSS-E2E-010 — Exportação portátil

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android, Persistência/Backup

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Criar dataset completo e exportar backup.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Arquivo contém dados necessários para reproduzir métricas.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-011 — Restauração em instalação limpa

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android, Persistência/Backup

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Importar backup.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Todos os 7 objetivos retornam mesmos resultados.
- A restauração não pode produzir perda silenciosa nem duplicação de registros.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-012 — Backup corrompido

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android, Persistência/Backup

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Tentar restaurar arquivo alterado.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Checksum/validação bloqueia restauração sem apagar base atual.
- A restauração não pode produzir perda silenciosa nem duplicação de registros.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-013 — Migração de schema preserva métricas

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Iniciar o teste em uma versão anterior conhecida do schema com dados válidos já persistidos.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Atualizar app com base existente.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Resultados financeiros anteriores continuam iguais.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-014 — OCR → custo → orçamento

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android, OCR

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Usar fixture de imagem/PDF determinística e legível; nenhum dado deve ser persistido antes da confirmação quando o cenário assim exigir.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Escanear nota, confirmar insumo e criar receita.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Preço do insumo confirmado alimenta custo corretamente.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Fixture utilizada e tela de revisão do OCR antes da confirmação.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-015 — OCR corrigido manualmente → cálculo

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android, OCR

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Usar fixture de imagem/PDF determinística e legível; nenhum dado deve ser persistido antes da confirmação quando o cenário assim exigir.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Corrigir valor reconhecido antes de salvar.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Cálculo usa valor corrigido.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Fixture utilizada e tela de revisão do OCR antes da confirmação.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-016 — Nota duplicada → sem inflação artificial

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android, OCR

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Usar fixture de imagem/PDF determinística e legível; nenhum dado deve ser persistido antes da confirmação quando o cenário assim exigir.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Importar documento repetido.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Histórico e custo de referência não duplicam compra.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Fixture utilizada e tela de revisão do OCR antes da confirmação.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## CROSS-E2E-017 — PDF cliente não vaza custo

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android, PDF

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Gerar orçamento PDF.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Não contém custo, lucro, margem, hora, gás ou energia.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo PDF gerado e o trecho visual onde o valor testado aparece.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-018 — PDF cliente contém preço correto

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android, PDF

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Gerar PDF da massa canônica.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Total do cliente coincide com preço de venda persistido.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Arquivo PDF gerado e o trecho visual onde o valor testado aparece.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-019 — Compartilhamento não altera orçamento

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Abrir Sharesheet/WhatsApp e voltar.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Nenhum cálculo ou status muda sem ação explícita.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-020 — Aprovação gera exatamente um pedido

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Tocar aprovação repetidamente/duplo toque.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Somente um ProductionOrder é criado.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-021 — Conclusão gera exatamente uma venda

**Prioridade:** P1  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Tentar duplo toque em finalizar.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Somente uma Sale é persistida.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-022 — Formatação pt-BR

**Prioridade:** P2  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Navegar por telas financeiras.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- R$ e separadores aparecem consistentemente.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-023 — Acessibilidade com fonte ampliada

**Prioridade:** P2  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Configurar escala de fonte do sistema para o maior valor suportado pelo plano de testes.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Ativar fonte grande.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Valores críticos e CTAs continuam legíveis/operáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-024 — Reduce Motion

**Prioridade:** P2  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Ativar a preferência de redução de movimento do sistema antes de abrir o app.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Ativar reduzir movimento.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Fluxos continuam funcionais sem depender de animações.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-025 — Rotação de tela durante wizard

**Prioridade:** P2  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Rotacionar dispositivo durante preenchimento.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Estado não é perdido/duplicado.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-026 — Process death após salvar

**Prioridade:** P2  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- O ambiente de teste deve permitir encerrar completamente o processo e iniciar uma nova instância do app.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Forçar morte do processo.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Dados já confirmados permanecem.
- dados não confirmados seguem política de draft.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-027 — Grande histórico

**Prioridade:** P2  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Pré-carregar dataset determinístico em volume suficiente para estresse sem alterar os valores esperados do cenário.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Popular milhares de vendas/preços.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Dashboards retornam dados corretos sem timeout perceptível.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---

## CROSS-E2E-028 — Sem dados no financeiro

**Prioridade:** P2  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Abrir instalação nova.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.

### Resultados esperados
- Mostrar estado vazio.
- não mostrar métricas enganadoras.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-029 — Consistência Android → web via backup

**Prioridade:** P2  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android, Web, Persistência/Backup

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Disponibilizar diretório de teste gravável para exportação/importação do backup.
- Disponibilizar a interface web com um ambiente de dados vazio para importação do mesmo conjunto usado no Android.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Exportar Android, importar web.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Todos os KPIs calculados do mesmo período coincidem.
- A restauração não pode produzir perda silenciosa nem duplicação de registros.
- Os valores exibidos na web devem coincidir centavo a centavo com a origem quando a mesma regra de agregação for aplicada.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.
- Screenshot Android e screenshot Web do mesmo período/filtro.
- Arquivo de backup utilizado e evidência do resultado após restauração.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.

---

## CROSS-E2E-030 — Timezone e datas de entrega

**Prioridade:** P2  
**Objetivo validado:** Garantir consistência ponta a ponta entre orçamento, produção, venda, histórico e financeiro.  
**Plataformas/áreas:** Android

### Pré-condições
- Banco de teste isolado ou explicitamente preparado pelo cenário.
- Todas as etapas principais estão disponíveis: orçamento, aprovação, produção, venda, histórico e financeiro.
- Persistência local e snapshots históricos estão habilitados.
- Fixar relógio e timezone do ambiente de teste conforme o cenário.

### Massa de dados
- Receita canônica com custo previsto R$ 70,90.
- Preço canônico R$ 118,17.
- Execução com estouro: custo real R$ 83,37; lucro R$ 34,80; margem 29,45%.
- Execução com economia: custo real R$ 65,90; lucro R$ 52,27.

### Passos
1. Iniciar o aplicativo em estado compatível com as pré-condições do cenário.
2. Confirmar que não existem registros residuais capazes de alterar agregações do cenário.
3. Criar pedido próximo da meia-noite.
4. Confirmar/salvar a ação quando aplicável e aguardar o estado final estável da interface.
5. Navegar até a tela que apresenta o resultado esperado e registrar os valores/estados exibidos.
6. Executar a segunda leitura do dado no contexto histórico/persistido exigido pelo cenário e comparar com a primeira leitura.

### Resultados esperados
- Produção e histórico usam data local correta.
- Valores históricos persistidos antes da alteração devem permanecer imutáveis.
- Nenhum registro adicional deve ser criado além dos explicitamente produzidos pelo fluxo.
- A UI deve terminar em estado estável, sem loading infinito ou ação pendente indevida.

### Evidências a capturar
- Screenshot da tela final com o valor/estado principal.
- Identificador do orçamento, pedido, venda ou ingrediente envolvido, quando existir.
- Valores exibidos antes/depois quando o cenário validar alteração ou preservação histórica.

### Critérios de falha
- O resultado principal diverge do valor ou estado esperado.
- O sistema persiste parcialmente uma operação que deveria ser atômica.
- Um dado histórico muda depois de uma alteração que deveria afetar apenas registros futuros.

---
