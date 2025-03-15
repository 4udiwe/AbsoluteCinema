package com.example.absolutecinema.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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


/**
 * Экран регистрации.
 * Имеет поля:
 * - email
 * - password
 * - comfirm password
 */
@Preview(showSystemUi = true)
@Composable
fun RegistrationScreen(
    onToLogin: () -> Unit = {},
    onEnter: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
    ) {

        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.Registration),
                    color = colorResource(R.color.text),
                    fontSize = 32.sp,
                    modifier = Modifier.padding(12.dp)
                )

                OutlinedTextField(
                    value = "",
                    placeholder = { Text(stringResource(R.string.mockemail)) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(R.color.accent),
                        focusedIndicatorColor = colorResource(R.color.accent),
                        unfocusedContainerColor = colorResource(R.color.background),
                        focusedContainerColor = colorResource(R.color.background),
                        unfocusedTrailingIconColor = colorResource(R.color.text_second),
                        focusedTrailingIconColor = colorResource(R.color.text_second),
                        unfocusedPlaceholderColor = colorResource(R.color.text_second),
                        focusedPlaceholderColor = colorResource(R.color.text_second),
                    ),
                    onValueChange = {}

                )
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(stringResource(R.string.Password)) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(R.color.accent),
                        focusedIndicatorColor = colorResource(R.color.accent),
                        unfocusedContainerColor = colorResource(R.color.background),
                        focusedContainerColor = colorResource(R.color.background),
                        unfocusedTrailingIconColor = colorResource(R.color.text_second),
                        focusedTrailingIconColor = colorResource(R.color.text_second),
                        unfocusedPlaceholderColor = colorResource(R.color.text_second),
                        focusedPlaceholderColor = colorResource(R.color.text_second),
                    ),
                    onValueChange = {}
                )
                OutlinedTextField(
                    value = "",
                    placeholder = { Text(stringResource(R.string.ConfirmPassword)) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(R.color.accent),
                        focusedIndicatorColor = colorResource(R.color.accent),
                        unfocusedContainerColor = colorResource(R.color.background),
                        focusedContainerColor = colorResource(R.color.background),
                        unfocusedTrailingIconColor = colorResource(R.color.text_second),
                        focusedTrailingIconColor = colorResource(R.color.text_second),
                        unfocusedPlaceholderColor = colorResource(R.color.text_second),
                        focusedPlaceholderColor = colorResource(R.color.text_second),
                    ),
                    onValueChange = {}
                )


                Row {
                    Text(
                        text = stringResource(R.string.HasAccount),
                        color = colorResource(R.color.text)
                    )
                    Text(text = stringResource(R.string.ToLogin),
                        color = colorResource(R.color.accent),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                onToLogin.invoke()
                            })
                }
            }

        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.accent)),
            onClick = {
                onEnter.invoke()
            }) {
            Text(
                stringResource(R.string.Registrate),
                color = colorResource(R.color.text),
                maxLines = 1
            )
        }
    }
}