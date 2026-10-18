const brl = new Intl.NumberFormat('pt-BR',{style:'currency',currency:'BRL'});
const pct = value => `${Number(value||0).toLocaleString('pt-BR',{maximumFractionDigits:2})}%`;
const money = cents => brl.format(Number(cents||0)/100);
const requiredArrays = ['sales','priceTrends'];

document.querySelectorAll('nav button').forEach(button=>button.addEventListener('click',()=>{
  document.querySelectorAll('nav button').forEach(item=>item.removeAttribute('aria-current'));
  document.querySelectorAll('.view').forEach(view=>view.classList.remove('active'));
  button.setAttribute('aria-current','page');
  document.getElementById(button.dataset.view).classList.add('active');
}));

document.getElementById('import-file').addEventListener('change',async event=>{
  try { render(validate(JSON.parse(await event.target.files[0].text()))); }
  catch (error) { alert(`Arquivo inválido: ${error.message}`); }
  finally { event.target.value=''; }
});

function validate(data){
  if(!data || typeof data!=='object') throw new Error('conteúdo ausente');
  if(Number(data.schemaVersion)!==1) throw new Error('versão não suportada');
  requiredArrays.forEach(key=>{if(!Array.isArray(data[key])) throw new Error(`${key} ausente`)});
  return data;
}

function render(data){
  const sales=data.sales;
  const revenue=sales.reduce((sum,s)=>sum+Number(s.salePriceCents||0),0);
  const cost=sales.reduce((sum,s)=>sum+Number(s.actualCostCents||0),0);
  const profit=revenue-cost;
  const metrics=[['Faturamento',money(revenue)],['Custo real',money(cost)],['Lucro',money(profit)],['Ticket médio',money(sales.length?Math.round(revenue/sales.length):0)]];
  document.getElementById('metrics').innerHTML=metrics.map(([label,value])=>`<article class="metric"><span>${escapeHtml(label)}</span><strong>${escapeHtml(value)}</strong></article>`).join('');
  document.getElementById('history-body').innerHTML=sales.slice().sort((a,b)=>b.completedAtMillis-a.completedAtMillis).map(s=>`<tr><td>${new Date(s.completedAtMillis).toLocaleDateString('pt-BR')}</td><td>${escapeHtml(s.productName)}</td><td>${money(s.salePriceCents)}</td><td>${money(s.actualCostCents)}</td><td class="${s.profitCents<0?'negative':'positive'}">${money(s.profitCents)}</td></tr>`).join('');
  const grouped=Object.values(sales.reduce((map,s)=>{const item=map[s.productName]??={name:s.productName,revenue:0,profit:0,count:0};item.revenue+=Number(s.salePriceCents);item.profit+=Number(s.profitCents);item.count++;return map},{})).sort((a,b)=>b.profit-a.profit);
  document.getElementById('ranking-list').innerHTML=grouped.map((item,index)=>`<li><span><b>${index+1}. ${escapeHtml(item.name)}</b><br><small>${item.count} venda(s)</small></span><strong>${money(item.profit)}</strong></li>`).join('');
  document.getElementById('trend-list').innerHTML=data.priceTrends.map(t=>`<li>${escapeHtml(t.ingredientName)}: <b class="${t.variationPercent>0?'negative':'positive'}">${pct(t.variationPercent)}</b></li>`).join('')||'<li>Sem histórico suficiente.</li>';
  const estimated=sales.reduce((sum,s)=>sum+Number(s.estimatedCostCents||0),0);
  document.getElementById('deviation-summary').textContent=`Previsto ${money(estimated)} · Real ${money(cost)} · Desvio ${money(cost-estimated)}`;
  drawChart(sales);
  document.getElementById('empty').hidden=true;
}

function drawChart(sales){
  const svg=document.getElementById('revenue-chart');
  if(!sales.length){svg.innerHTML='<text x="20" y="40">Sem vendas no período</text>';return}
  const ordered=sales.slice().sort((a,b)=>a.completedAtMillis-b.completedAtMillis);
  const max=Math.max(...ordered.map(s=>Number(s.salePriceCents)),1);
  const width=680/ordered.length;
  svg.innerHTML=ordered.map((s,index)=>{const height=180*Number(s.salePriceCents)/max;return `<rect x="${20+index*width}" y="${210-height}" width="${Math.max(8,width-8)}" height="${height}" rx="5" fill="#8b2d3f"><title>${escapeHtml(s.productName)}: ${money(s.salePriceCents)}</title></rect>`}).join('');
}

function escapeHtml(value){const node=document.createElement('span');node.textContent=String(value??'');return node.innerHTML}
