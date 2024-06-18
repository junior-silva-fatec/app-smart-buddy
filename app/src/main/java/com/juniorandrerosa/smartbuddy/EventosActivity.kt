package com.juniorandrerosa.smartbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juniorandrerosa.smartbuddy.ui.theme.SmartBuddyTheme

class EventosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartBuddyTheme {
                EventScreen(sampleEvents)
            }
        }
    }
}

val sampleEvents = listOf(
    Event(
        id = "6646ddbc5ed7f7ab46e81abe",
        title = "Evento do 234",
        description = "Descrição do evento vai aqui",
        date = "2024-05-17",
        duration = 1.5,
        time = "12:20",
        owner = "234@234.com"
    ),
    Event(
        id = "664d4961fc0798f61e2dbdca",
        title = "Aula de IoT",
        description = "Aula de IoT - Fatec",
        date = "2024-05-21",
        duration = 1.0,
        time = "03:30",
        owner = "234@234.com"
    ),
    Event(
        id = "6651916e068bbb5cb7f5834d",
        title = "Novo Evento",
        description = "Nova descrição",
        date = "2024-05-25",
        duration = 1.0,
        time = "06:27",
        owner = "234@234.com"
    ),
    Event(
        id = "665e17c8068bbb5cb7f58389",
        title = "Mais um evento",
        description = "Mais um evento. Mais um evento",
        date = "2024-06-03",
        duration = 1.0,
        time = "16:30",
        owner = "234@234.com"
    ),
    Event(
        id = "665e2480068bbb5cb7f583b4",
        title = "Evento 20 de maio",
        description = "Descrição evento dia 20 de maio",
        date = "2024-05-20",
        duration = 2.0,
        time = "20:15",
        owner = "234@234.com"
    )
)

@Composable
fun EventCard(event: Event) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = event.date, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun EventScreen(events: List<Event>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(events) { event ->
            EventCard(event)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SmartBuddyTheme {
        EventScreen(sampleEvents)
    }
}
