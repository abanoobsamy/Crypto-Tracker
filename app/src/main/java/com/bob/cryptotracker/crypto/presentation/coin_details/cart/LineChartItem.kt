package com.bob.cryptotracker.crypto.presentation.coin_details.cart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bob.cryptotracker.crypto.domain.CoinPrice
import com.bob.cryptotracker.ui.theme.CryptoTrackerTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun LineChartItem(
    dataPoints: List<DataPoint>,
    style: ChartStyle,
    visibleDataIndices: IntRange,
    unit: String,
    modifier: Modifier = Modifier,
    selectedDataPoint: DataPoint? = null,
    onDataPointSelected: (DataPoint) -> Unit = {},
    onXLabelWidthChange: (Float) -> Unit = {},
    showHelperLines: Boolean = true,
) {
    val textStyle = LocalTextStyle.current.copy(
        fontSize = style.labelFontSize
    )

    val visibleDataPoints = remember(dataPoints, visibleDataIndices) {
        dataPoints.slice(visibleDataIndices)
    }

    val maxYValue = remember(visibleDataPoints) {
        visibleDataPoints.maxOfOrNull { it.y } ?: 0f
    }
    val minYValue = remember(visibleDataPoints) {
        visibleDataPoints.minOfOrNull { it.y } ?: 0f
    }

    val measurer = rememberTextMeasurer()

    var xLabelWidth by remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(key1 = xLabelWidth) {
        onXLabelWidthChange(xLabelWidth)
    }

    val selectedDataPointIndex = remember(selectedDataPoint) {
        dataPoints.indexOf(selectedDataPoint)
    }

    var drawPoints by remember {
        mutableStateOf(listOf<DataPoint>())
    }
    var isShowingDataPoints by remember {
        mutableStateOf(selectedDataPoint != null)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val minLabelSpacingYPx = style.minYLabelSpacing.roundToPx()
        val verticalPaddingPx = style.verticalPadding.roundToPx()
        val horizontalPaddingPx = style.horizontalPadding.roundToPx()
        val xAxisLabelSpacingPx = style.xAxisLabelSpacing.toPx()

        val xLabelTextLayoutRes = visibleDataPoints.map {
            measurer.measure(
                text = it.xLabel,
                style = textStyle.copy(textAlign = TextAlign.Center)
            )
        }

        val maxXLabelWidth = xLabelTextLayoutRes.maxOfOrNull { it.size.width } ?: 0
        val maxXLabelHeight = xLabelTextLayoutRes.maxOfOrNull { it.size.height } ?: 0
        val maxXLabelLineCount = xLabelTextLayoutRes.maxOfOrNull { it.lineCount } ?: 0
        val xLabelLineHeight = maxXLabelHeight / maxXLabelLineCount

        val viewPortHeightPx = size.height -
                (maxXLabelHeight + 2 * verticalPaddingPx
                        + xLabelLineHeight + xAxisLabelSpacingPx)

        val viewPortTopY = verticalPaddingPx + xLabelLineHeight + 10f
        val viewPortRightY = size.width
        val viewPortBottomY = viewPortTopY + viewPortHeightPx
        val viewPortLeftY = 2f * horizontalPaddingPx

        val viewPort = Rect(
            left = viewPortLeftY,
            top = viewPortTopY,
            right = viewPortRightY,
            bottom = viewPortBottomY,
        )

        drawRect(
            color = Color.Green,
            topLeft = viewPort.topLeft,
            size = viewPort.size,
        )

        xLabelWidth = maxXLabelWidth + xAxisLabelSpacingPx
        xLabelTextLayoutRes.forEachIndexed { index, result ->
            drawText(
                textLayoutResult = result,
                topLeft = Offset(
                    x = viewPortLeftY + xAxisLabelSpacingPx / 2f
                            + xLabelWidth * index,
                    y = viewPortBottomY + xAxisLabelSpacingPx
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LineChartItemPreview() {
    CryptoTrackerTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val coinHistoryRandomized = remember {
                (1..20).map {
                    CoinPrice(
                        priceUsd = Random.nextFloat() * 1000.0,
                        dateTime = ZonedDateTime.now().plusHours(it.toLong()),
                    )
                }
            }
            val style = ChartStyle(
                chartLineColor = Color.Black,
                unselectedColor = Color(0xFF7c7c7),
                selectedColor = Color.Black,
                helperLineThicknessPx = 5f,
                axisLineThicknessPx = 5f,
                labelFontSize = 14.sp,
                minYLabelSpacing = 24.dp,
                verticalPadding = 8.dp,
                horizontalPadding = 8.dp,
                xAxisLabelSpacing = 8.dp
            )
            val dataPoints = remember {
                coinHistoryRandomized.map {
                    DataPoint(
                        x = it.dateTime.toEpochSecond().toFloat(),
                        y = it.priceUsd.toFloat(),
                        xLabel = DateTimeFormatter
                            .ofPattern("ha\nM/d")
                            .format(it.dateTime)
                    )
                }
            }
            LineChartItem(
                dataPoints = dataPoints,
                style = style,
                visibleDataIndices = 0..19,
                unit = "$",
                modifier = Modifier
                    .width(700.dp)
                    .height(300.dp)
                    .padding(innerPadding),
                selectedDataPoint = dataPoints[1],
            )
        }
    }
}