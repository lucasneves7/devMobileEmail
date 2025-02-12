package com.example.neves.oliveira.lucas.email

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.neves.oliveira.lucas.email.ui.theme.NevesOliveiraLucasEmailTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NevesOliveiraLucasEmailTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContentScreen(Modifier.fillMaxSize().padding(innerPadding), this)

                }
            }
        }
    }
}

@Composable
private fun ContentScreen(modifier: Modifier, context: Context){
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    var email by rememberSaveable("email") { mutableStateOf("") }
    var topic by rememberSaveable("topic") { mutableStateOf("") }
    var message by rememberSaveable("message") { mutableStateOf("") }

    ConstraintLayout(modifier = modifier) {
        val (emailRef, topicRef, messageRef, btnRef) = createRefs()

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = Modifier.constrainAs(emailRef){
                val endRef = if(isPortrait) parent.end else btnRef.start
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(endRef, margin = 16.dp)
                width = Dimension.fillToConstraints
            }
        )

        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text(text = "Assunto") },
            modifier = Modifier.constrainAs(topicRef){
                val endRef = if(isPortrait) parent.end else btnRef.start
                top.linkTo(emailRef.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(endRef, margin = 16.dp)
                width = Dimension.fillToConstraints
            }
        )

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text(text = "Mensagem") },
            modifier = Modifier.constrainAs(messageRef){
                val endRef = if(isPortrait) parent.end else btnRef.start
                val bottomRef = if(isPortrait) btnRef.top else parent.bottom
                top.linkTo(topicRef.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(endRef, margin = 16.dp)
                bottom.linkTo(bottomRef, margin = 16.dp)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }
        )

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = android.net.Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                intent.putExtra(Intent.EXTRA_SUBJECT, topic)
                intent.putExtra(Intent.EXTRA_TEXT, message)

                try {
                    context.startActivity(intent)
                }catch (e: Exception){
                    Toast.makeText(context, "Erro ao enviar e-mail, nenhum aplicativo encontrado", Toast.LENGTH_SHORT).show()
                }

            },
            modifier = Modifier.constrainAs(btnRef){
                val startRef = if(isPortrait) parent.start else emailRef.end
                start.linkTo(startRef, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
                width = if(isPortrait) Dimension.fillToConstraints else Dimension.wrapContent
            }
        ) {
            Text("Enviar")
        }
    }
}