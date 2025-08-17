package com.example.lab5

import android.content.Context
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current


    val nombreCompleto = "Diego Sebastián Guevara Casasola"
    val restaurante = "Sushiito"   // <- antes "Ookii"
    val direccionCorta = "22 calle 9-33, Zona 16, C.C. Paseo Cayalá, local J3, 105" // <- nueva dirección
    val horario = "8:00AM 7:00PM"
    val tipoComida = "Sushi"
    val precio = "QQ" // Q, QQ o QQQ

    val lat = 14.6063
    val lng = -90.4819
// =================================

    // Cumpleaños (día y mes) – la app mostrará la fecha de ESTE AÑO:
    val cumpleDia = 28
    val cumpleMes = 11 // noviembre (1 = enero … 12 = diciembre)
    // ==========================================================

    val hoyCumple = remember {
        LocalDate.of(LocalDate.now().year, cumpleMes, cumpleDia)
    }

    val localeEs = Locale("es", "ES")
    val fmtDia = DateTimeFormatter.ofPattern("EEEE", localeEs)
    val fmtFecha = DateTimeFormatter.ofPattern("d 'de' MMMM", localeEs)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = "Actualizar")
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Actualización disponible",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = {
                        openPlayStore(context, "com.whatsapp")
                    }
                ) { Text("Descargar") }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Encabezado: día de la semana + fecha (del cumpleaños de este año)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = hoyCumple.format(fmtDia).replaceFirstChar { it.titlecase(localeEs) },
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = hoyCumple.format(fmtFecha),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            OutlinedButton(onClick = { /* sin acción requerida */ }) {
                Text("Terminar jornada")
            }
        }

        Spacer(Modifier.height(12.dp))

        // Tarjeta del restaurante
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = restaurante,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(direccionCorta, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(horario, style = MaterialTheme.typography.labelLarge)

                    Spacer(Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "¡Hola, $nombreCompleto!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) { Text("Iniciar") }

                        TextButton(
                            onClick = {
                                val msg = "Tipo de comida: $tipoComida\nQué tan caro es: $precio"
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            }
                        ) { Text("Detalles") }
                    }
                }

                // Ícono de "Directions" arriba a la derecha
                IconButton(
                    onClick = { openGoogleMaps(context, lat, lng, restaurante) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(Icons.Rounded.Place, contentDescription = "Directions")
                }

            }
        }
    }
}

// ---- Helpers ----
private fun openPlayStore(context: Context, packageName: String) {
    val market = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    market.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        ContextCompat.startActivity(context, market, null)
    } catch (e: ActivityNotFoundException) {
        val web = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        )
        web.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(context, web, null)
    }
}

private fun openGoogleMaps(
    context: Context,
    lat: Double,
    lng: Double,
    label: String
) {
    // geo:lat,lng?q=lat,lng(label) abre pin directo
    val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng($label)")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps") // intenta abrir Google Maps
    try {
        ContextCompat.startActivity(context, intent, null)
    } catch (e: ActivityNotFoundException) {
        // Fallback al navegador
        val web = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
        )
        ContextCompat.startActivity(context, web, null)
    }
}
