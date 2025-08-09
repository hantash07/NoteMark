package com.hantash.notemark.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.hantash.notemark.R
import com.hantash.notemark.ui.component.EnumSettingsItem.LOGOUT
import com.hantash.notemark.ui.component.EnumSettingsItem.SYNC_DATA
import com.hantash.notemark.ui.component.EnumSettingsItem.SYNC_INTERVAL
import com.hantash.notemark.ui.theme.OnSurface
import com.hantash.notemark.ui.theme.OnSurfaceOpacity12
import com.hantash.notemark.ui.theme.OnSurfaceVariant
import com.hantash.notemark.ui.theme.Primary
import com.hantash.notemark.ui.theme.Surface
import com.hantash.notemark.ui.theme.SurfaceLowest
import com.hantash.notemark.ui.theme.Tertiary

@Preview(showBackground = true)
@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    enumItem: EnumSettingsItem = SYNC_INTERVAL,
    title: String = enumItem.title,
    description: String = "",
    icon: Int = R.drawable.ic_logout,
    selectedSyncInterval: SyncInterval = SyncInterval.ManualOnly,
    isExpandSyncOption: Boolean = false,
    onSelectInterval: (SyncInterval) -> Unit = {},
    onClick: (EnumSettingsItem) -> Unit = {},
) {
    val color = if (enumItem == LOGOUT) Tertiary else OnSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (enumItem == SYNC_DATA) 78.dp else 56.dp)
            .clickable { onClick.invoke(enumItem) },
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Settings Icon",
                tint = color
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(color = color)
                )
                if (enumItem == SYNC_DATA) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                    )
                }
            }

            if (enumItem == SYNC_INTERVAL) {
                Box {
                    Row(
                        modifier = Modifier
                            .clickable {
                                onClick(enumItem)
                            }
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = selectedSyncInterval.label,
                            style = MaterialTheme.typography.bodyLarge.copy(color = OnSurfaceVariant)
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_next),
                            contentDescription = "Next Icon",
                        )
                    }

                    DropdownMenu(
                        expanded = isExpandSyncOption,
                        offset = DpOffset(x = 0.dp, y = 16.dp),
                        containerColor = SurfaceLowest,
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 8.dp,
                        onDismissRequest = { onClick(enumItem) }
                    ) {
                        SyncInterval.entries.toList().forEach { syncInterval ->
                            DropdownMenuItem(
                                modifier = Modifier.width(190.dp),
                                contentPadding = PaddingValues(all = 16.dp),
                                text = {
                                    Text(
                                        syncInterval.label,
                                        style = MaterialTheme.typography.bodyLarge.copy(color = OnSurface)
                                    )
                                },
                                onClick = {
                                    onSelectInterval(syncInterval)
                                },
                                trailingIcon = {
                                    if (syncInterval == selectedSyncInterval) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Primary
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        if (enumItem != LOGOUT) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                color = OnSurfaceOpacity12
            )
        }
    }
}

enum class EnumSettingsItem(val title: String) {
    SYNC_INTERVAL(title = "Sync Interval"),
    SYNC_DATA(title = "Sync Data"),
    LOGOUT(title = "Log Out");
}

enum class SyncInterval(val label: String, val minutes: Int?) {
    ManualOnly("Manual only", null),
    Every15Min("15 minutes", 15),
    Every30Min("30 minutes", 30),
    Every1Hour("1 hour", 60);

    companion object {
        val allOptions: List<String>
            get() = entries.map { it.label }

        fun fromLabel(label: String?): SyncInterval =
            entries.firstOrNull { it.label == label } ?: ManualOnly
    }
}
