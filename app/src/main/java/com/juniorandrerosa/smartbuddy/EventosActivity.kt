package com.juniorandrerosa.smartbuddy

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juniorandrerosa.smartbuddy.ui.theme.SmartBuddyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class EventosActivity : ComponentActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartBuddyTheme {
                val email = intent.getStringExtra("email") ?: ""
                var events by remember { mutableStateOf<List<Event>>(emptyList()) }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(email) {
                    events = fetchEvents(email)
                    isLoading = false
                }

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                } else {
                    val coroutineScope = rememberCoroutineScope()
                    EventScreen(events, onDelete = { eventId ->
                        coroutineScope.launch {
                            val success = deleteEvent(eventId)
                            if (success) {
                                events = events.filter { it.id != eventId }
                            }
                        }
                    })
                }
            }
        }
    }

    private suspend fun fetchEvents(email: String): List<Event> {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://web-qx4yu7fnv0m1.up-us-nyc1-k8s-1.apps.run-on-seenode.com/events/owner/$email")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val jsonArray = JSONArray(responseBody)
                    val events = mutableListOf<Event>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val event = Event(
                            id = jsonObject.getString("_id"),
                            title = jsonObject.getString("title"),
                            description = jsonObject.getString("description"),
                            date = jsonObject.getString("date"),
                            duration = jsonObject.getDouble("duration"),
                            time = jsonObject.getString("time"),
                            owner = jsonObject.getString("owner")
                        )
                        events.add(event)
                    }
                    events
                } else {
                    emptyList()
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EventosActivity, "Erro ao carregar eventos: ${e.message}", Toast.LENGTH_LONG).show()
                }
                emptyList()
            }
        }
    }

    private suspend fun deleteEvent(eventId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://web-qx4yu7fnv0m1.up-us-nyc1-k8s-1.apps.run-on-seenode.com/events/$eventId")
                .delete()
                .build()

            try {
                val response = client.newCall(request).execute()
                response.isSuccessful
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EventosActivity, "Erro ao deletar evento: ${e.message}", Toast.LENGTH_LONG).show()
                }
                false
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onDelete: (String) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = event.date, style = MaterialTheme.typography.bodyMedium)
            Button(
                onClick = { onDelete(event.id) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun EventScreen(events: List<Event>, onDelete: (String) -> Unit) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        items(events) { event ->
            EventCard(event) { eventId ->
                onDelete(eventId)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SmartBuddyTheme {
        EventScreen(
            listOf(
                Event(
                    id = "1",
                    title = "Sample Event",
                    description = "This is a sample event description",
                    date = "2024-05-17",
                    duration = 1.5,
                    time = "12:20",
                    owner = "owner@example.com"
                )
            ),
            onDelete = {}
        )
    }
}
