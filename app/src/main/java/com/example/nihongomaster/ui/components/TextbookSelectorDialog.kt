package com.example.nihongomaster.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.nihongomaster.model.TextbookSource
import com.example.nihongomaster.ui.theme.LacquerRed

@Composable
fun TextbookSelectorDialog(
    selectedTextbook: TextbookSource,
    onSelectTextbook: (TextbookSource) -> Unit,
    onDismiss: () -> Unit
) {
    val textbooks = listOf(
        TextbookSource.KANZEN_MASTER to "Kurikulum resmi dengan analisis tata bahasa terperinci dan latihan gaya ujian.",
        TextbookSource.SOU_MATOME to "Format terstruktur 6 minggu belajar santai dengan tema mingguan.",
        TextbookSource.MINNA_NO_NIHONGO to "Standar fondasi bahasa Jepang untuk pemula hingga menengah (N5/N4).",
        TextbookSource.NIHONGO_NO_MORI to "Penjelasan ringkas dan aplikatif oleh sensei native Jepang.",
        TextbookSource.TRY_N2 to "Fokus persiapan praktis pola kalimat untuk ujian kelulusan N2.",
        TextbookSource.ALL_IN_ONE to "Kombinasi seluruh sumber buku kurikulum terbaik."
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pilih Buku Kurikulum",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                Text(
                    text = "Materi dan latihan akan disesuaikan dengan metodologi buku yang kamu pilih.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    items(textbooks) { (source, desc) ->
                        val isSelected = source == selectedTextbook
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) LacquerRed.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) ButtonDefaults.outlinedButtonBorder else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onSelectTextbook(source)
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = source.label,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) LacquerRed else MaterialTheme.colorScheme.onSurface
                                        )
                                        Badge(containerColor = if (isSelected) LacquerRed else MaterialTheme.colorScheme.outline) {
                                            Text(source.level, color = MaterialTheme.colorScheme.surface, fontSize = 10.sp)
                                        }
                                    }
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Terpilih",
                                        tint = LacquerRed,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
