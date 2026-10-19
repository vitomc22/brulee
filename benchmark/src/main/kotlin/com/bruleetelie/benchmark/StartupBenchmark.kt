package com.bruleetelie.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupBenchmark {
    @get:Rule val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun coldStartupAndFinanceScroll() = benchmarkRule.measureRepeated(
        packageName = "com.bruleetelie.app",
        metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        startupMode = StartupMode.COLD,
        iterations = 5,
        setupBlock = { pressHome() }
    ) {
        startActivityAndWait()
        check(device.wait(Until.hasObject(By.desc("Navegação Finanças")), 5_000)) {
            "Navegação Finanças não ficou disponível"
        }
        device.findObject(By.desc("Navegação Finanças")).click()
        device.waitForIdle()
        device.swipe(device.displayWidth / 2, device.displayHeight * 3 / 4, device.displayWidth / 2, device.displayHeight / 4, 20)
    }

    @Test
    fun coldStartupAndQuoteWizard() = benchmarkRule.measureRepeated(
        packageName = "com.bruleetelie.app",
        metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        startupMode = StartupMode.COLD,
        iterations = 5,
        setupBlock = { pressHome() }
    ) {
        startActivityAndWait()
        check(device.wait(Until.hasObject(By.desc("Navegação Novo Orçamento")), 5_000)) {
            "Navegação Novo Orçamento não ficou disponível"
        }
        device.findObject(By.desc("Navegação Novo Orçamento")).click()
        device.waitForIdle()
    }

    @Test
    fun coldStartupAndProductionQueue() = benchmarkRule.measureRepeated(
        packageName = "com.bruleetelie.app",
        metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        startupMode = StartupMode.COLD,
        iterations = 5,
        setupBlock = { pressHome() }
    ) {
        startActivityAndWait()
        check(device.wait(Until.hasObject(By.desc("Navegação Produção")), 5_000)) {
            "Navegação Produção não ficou disponível"
        }
        device.findObject(By.desc("Navegação Produção")).click()
        device.waitForIdle()
    }

    @Test
    fun coldStartupAndOrders() = benchmarkRule.measureRepeated(
        packageName = "com.bruleetelie.app",
        metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        startupMode = StartupMode.COLD,
        iterations = 5,
        setupBlock = { pressHome() }
    ) {
        startActivityAndWait()
        check(device.wait(Until.hasObject(By.desc("Navegação Pedidos")), 5_000)) {
            "Navegação Pedidos não ficou disponível"
        }
        device.findObject(By.desc("Navegação Pedidos")).click()
        device.waitForIdle()
    }

    @Test
    fun coldStartupAndShopping() = benchmarkRule.measureRepeated(
        packageName = "com.bruleetelie.app",
        metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        startupMode = StartupMode.COLD,
        iterations = 5,
        setupBlock = { pressHome() }
    ) {
        startActivityAndWait()
        check(device.wait(Until.hasObject(By.desc("Navegação Estoque")), 5_000)) {
            "Navegação Estoque não ficou disponível"
        }
        device.findObject(By.desc("Navegação Estoque")).click()
        device.waitForIdle()
    }
}
