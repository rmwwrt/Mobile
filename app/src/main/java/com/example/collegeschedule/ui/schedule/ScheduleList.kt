package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.LessonPartDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto

private val buildingColors = mapOf(
    "1" to Color(0xFFE57373),
    "2" to Color(0xFF81C784),
    "3" to Color(0xFF64B5F6),
    "4" to Color(0xFFFFF176),
    "5" to Color(0xFF9575CD),
    "6" to Color(0xFF4DB6AC),
    "7" to Color(0xFFF06292),
    "8" to Color(0xFFFFB74D),
    "9" to Color(0xFFA1887F),
    "10" to Color(0xFF7986CB)
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
    ) {
        data.forEach { day ->
            stickyHeader {
                Text(
                    text = "${day.lessonDate} (${day.weekday})",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            if (day.lessons.isEmpty()) {
                item {
                    Text(
                        text = "Информация отсутствует",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp)
                    )
                }
            } else {
                val allLessonParts = day.lessons.flatMap {
                    it.groupParts.values.filterNotNull()
                }
                items(allLessonParts) { lessonPart ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        LessonCardContent(lessonPart)
                    }
                }
            }
        }
    }
}

@Composable
fun LessonCardContent(info: LessonPartDto) {
    val buildingColor = buildingColors[info.building] ?: Color.Gray

    Row(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .size(4.dp, 64.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(buildingColor)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = info.subject,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = info.teacher,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${info.building}, ${info.classroom}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
