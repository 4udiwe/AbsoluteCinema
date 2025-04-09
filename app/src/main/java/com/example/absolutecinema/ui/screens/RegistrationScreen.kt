package com.example.absolutecinema.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.absolutecinema.R

@Preview(showSystemUi = true)
@Composable
fun RegistrationScreen(
    onToLogin: () -> Unit = {},
    onEnter: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(com.example.core.R.string.Registration),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(12.dp)
                )

                OutlinedTextField(value = "",
                    placeholder = { Text(stringResource(com.example.core.R.string.mockemail)) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(com.example.core.R.color.accent),
                        focusedIndicatorColor = colorResource(com.example.core.R.color.accent),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onValueChange = {}

                )
                OutlinedTextField(value = "",
                    placeholder = { Text(stringResource(com.example.core.R.string.Password)) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(com.example.core.R.color.accent),
                        focusedIndicatorColor = colorResource(com.example.core.R.color.accent),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onValueChange = {})
                OutlinedTextField(value = "",
                    placeholder = { Text(stringResource(com.example.core.R.string.ConfirmPassword)) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(com.example.core.R.color.accent),
                        focusedIndicatorColor = colorResource(com.example.core.R.color.accent),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onValueChange = {})


                Row {
                    Text(
                        text = stringResource(com.example.core.R.string.HasAccount),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = stringResource(com.example.core.R.string.ToLogin),
                        color = colorResource(
                            com.example.core.R.color.accent
                        ),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                onToLogin.invoke()
                            })
                }
            }

        }

        Button(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(com.example.core.R.color.accent)),
            onClick = {
                onEnter.invoke()
            }) {
            Text(
                stringResource(com.example.core.R.string.Registrate),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
        }
    }
}