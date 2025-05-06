package com.example.auth.ui


import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.viewmodel.AuthState
import com.example.auth.viewmodel.AuthViewModel
import com.example.core.R

@Composable
fun RegistrationScreen(
    onToLogin: () -> Unit = {},
    viewModel: AuthViewModel,
    onSuccess: () -> Unit,
    context: Context,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val authState = viewModel.authState.collectAsState()

    if (authState.value is AuthState.CreateAccountFail) {
        Toast.makeText(
            context, (authState.value as AuthState.CreateAccountFail).message, Toast.LENGTH_LONG
        ).show()
    }

    if (authState.value is AuthState.SignInSuccess)
        onSuccess.invoke()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (authState.value is AuthState.Loading)
            CircularProgressIndicator()

        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.Registration),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(12.dp)
                )

                OutlinedTextField(
                    value = email.value,
                    placeholder = { Text(stringResource(R.string.mockemail)) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            email.value = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(R.color.accent),
                        focusedIndicatorColor = colorResource(R.color.accent),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onValueChange = {
                        email.value = it
                    },
                    isError = authState.value is AuthState.CreateAccountFail

                )
                OutlinedTextField(
                    value = password.value,
                    placeholder = { Text(stringResource(R.string.Password)) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            password.value = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(R.color.accent),
                        focusedIndicatorColor = colorResource(R.color.accent),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onValueChange = {
                        password.value = it
                    },
                    isError = authState.value is AuthState.CreateAccountFail
                )
                OutlinedTextField(
                    value = confirmPassword.value,
                    placeholder = { Text(stringResource(R.string.ConfirmPassword)) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            confirmPassword.value = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(R.color.accent),
                        focusedIndicatorColor = colorResource(R.color.accent),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onValueChange = {
                        confirmPassword.value = it
                    },
                    isError = authState.value is AuthState.CreateAccountFail
                )

                Row {
                    Text(
                        text = stringResource(R.string.HasAccount),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = stringResource(R.string.ToLogin),
                        color = colorResource(
                            R.color.accent
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
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.accent)),
            onClick = {
                viewModel.createAccount(
                    email = email.value,
                    password = password.value,
                    confirmPassword = confirmPassword.value
                )
            }) {
            Text(
                stringResource(R.string.Registrate),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
        }
    }
}