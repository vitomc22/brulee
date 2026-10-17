package com.bruleetelie.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruleetelie.app.ui.components.StatusPill
import com.bruleetelie.app.ui.components.TactilePrimaryButton
import com.bruleetelie.app.ui.theme.*
import com.bruleetelie.domain.CostComparisonResult
import com.bruleetelie.domain.Money

@Composable
fun ProductionCompletionScreen(
    productName: String = "Brigadeiro Gourmet Tradicional",
    comparison: CostComparisonResult,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Celebratory Golden Banner (Streak & Bonus)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BruleeSecondaryContainer,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("🔥", style = MaterialTheme.typography.titleLarge)
                    Column {
                        Text(
                            "OFENSIVA INCRÍVEL",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BruleeOnSecondaryContainer
                        )
                        Text(
                            "7 dias seguidos!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BruleeOnSecondaryContainer
                        )
                    }
                }
                Surface(
                    color = BruleeSurfaceContainerLowest.copy(alpha = 0.85f),
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("⭐", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "+100 Pts",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = BruleeSecondary
                        )
                    }
                }
            }
        }

        // Mascot Showcase with Speech Bubble
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Speech Bubble
            Surface(
                color = BruleeSurfaceContainerLowest,
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Ficha finalizada com sucesso! ✨",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BruleePrimary
                    )
                    Text(
                        "Seus custos estão 100% blindados!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Mascot Avatar
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(BruleeSecondaryFixed.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👩‍🍳", style = MaterialTheme.typography.displayLarge)
                }
                Surface(
                    color = BruleePrimary,
                    shape = CircleShape,
                    shadowElevation = 2.dp
                ) {
                    Text(
                        "Nv. 14",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // XP Reward Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BruleeSurfaceContainerLowest,
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(BruleeSecondaryFixed),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("💎", style = MaterialTheme.typography.titleLarge)
                    }
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "+45 XP",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = BruleePrimary
                            )
                            Text("✓", color = BruleeSecondary, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            "Mestre Confeiteiro Brûlée",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                StatusPill("BÔNUS", BruleePrimaryFixed, BruleePrimary)
            }
        }

        // Card 2: Cost Accuracy Comparison Widget (Previsto vs Real)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BruleeSurfaceContainerLowest,
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("⚖", style = MaterialTheme.typography.titleMedium, color = BruleePrimary)
                        Text(
                            "Custo Real vs. Orçado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        productName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Financial Pills Grid
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BruleeSurfaceContainerLow, RoundedCornerShape(16.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = BruleeSurfaceContainerLowest,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                "Previsto (Orçado)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                comparison.estimatedCosts.total.format(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = BruleeSurfaceContainerLowest,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                "Real Apurado",
                                style = MaterialTheme.typography.labelSmall,
                                color = BruleePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                comparison.actualCosts.total.format(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = BruleePrimary
                            )
                        }
                    }
                }

                // Economy / Difference Pill
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (comparison.isCostHigher) BruleeErrorContainer else BruleePrimaryFixed.copy(alpha = 0.7f),
                    shape = CircleShape
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(if (comparison.isCostHigher) "⚠" else "💰")
                            Text(
                                if (comparison.isCostHigher)
                                    "Acréscimo de ${comparison.costDifference.format()}"
                                else
                                    "Economia de ${comparison.costDifference.format()}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        StatusPill(
                            text = "${if (comparison.marginDifferencePercent >= java.math.BigDecimal.ZERO) "+" else ""}${comparison.marginDifferencePercent}% margem",
                            background = if (comparison.isCostHigher) BruleeError else BruleePrimary,
                            foreground = Color.White
                        )
                    }
                }
            }
        }

        // Precision Streak Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BruleeSurfaceContainerLowest,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BruleeSecondaryFixed.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎯", style = MaterialTheme.typography.titleMedium)
                }
                Column {
                    Text("Streak de Precisão", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("12 receitas sem prejuízo", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // CTA
        TactilePrimaryButton(
            text = "CONTINUAR PARA O PAINEL",
            onClick = onContinue
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductionCompletionScreenPreview() {
    BruleeTheme {
        ProductionCompletionScreen(
            productName = "40 Brigadeiros Gourmet",
            comparison = CostComparisonResult(
                estimatedCosts = RecipeCosts(Money.fromReais("38.40"), Money.fromReais("50.00"), Money.fromReais("5.70"), Money.fromReais("4.50")),
                actualCosts = RecipeCosts(Money.fromReais("35.00"), Money.fromReais("50.00"), Money.fromReais("5.00"), Money.fromReais("4.50")),
                costDifference = Money.fromReais("3.60"),
                isCostHigher = false,
                salePrice = Money.fromReais("149.90"),
                estimatedProfit = Money.fromReais("51.30"),
                actualProfit = Money.fromReais("54.90"),
                estimatedMarginPercent = java.math.BigDecimal("34.22"),
                actualMarginPercent = java.math.BigDecimal("36.62"),
                marginDifferencePercent = java.math.BigDecimal("2.40")
            ),
            onContinue = {}
        )
    }
}
