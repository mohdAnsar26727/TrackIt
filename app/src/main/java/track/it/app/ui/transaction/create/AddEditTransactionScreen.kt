package track.it.app.ui.transaction.create

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import track.it.app.R
import track.it.app.domain.model.TransactionStatus
import track.it.app.ui.navigation.ScreenAddEditTransactions
import track.it.app.ui.theme.AppTypography
import track.it.app.ui.theme.marginMinimal
import track.it.app.ui.theme.marginNormal
import track.it.app.ui.theme.paddingMinimal
import track.it.app.ui.theme.paddingNormal
import track.it.app.ui.transaction.viewmodel.TransactionCUDViewmodel
import track.it.app.ui.util.DateFormat
import track.it.app.ui.util.formattedDate
import track.it.app.ui.util.toTitleCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    navController: NavController,
    args: ScreenAddEditTransactions,
    transactionViewModel: TransactionCUDViewmodel = hiltViewModel(),
    viewModel: AddEditTransactionViewModel = hiltViewModel()
) {
    if (args.id != null) {
        viewModel.setTransactionIdForEdit(args.id)
        viewModel.editTransaction.collectAsStateWithLifecycle()
    }
    val selectedImages = viewModel.selectedImagesList

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> viewModel.onImagePickerResult(uris) }
    )
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        },
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val selectedDate =
        datePickerState.selectedDateMillis?.formattedDate(DateFormat.DD_MM_YYYY) ?: ""

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    focusRequester.requestFocus()
                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun launchPhotoPicker() {
        multiplePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    LaunchedEffect(
        transactionViewModel.transactionResult,
        viewModel.transactionResult
    ) {
        if (transactionViewModel.transactionResult?.isSuccess == true
            || viewModel.transactionResult?.isSuccess == true
        ) {
            navController.navigateUp()
        } else if (viewModel.transactionResult != null || transactionViewModel.transactionResult != null) {
            scope.launch {
                snackbarHostState.showSnackbar("Something went wrong!")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (args.id == null) {
                            "Add Transaction"
                        } else {
                            "Edit Transaction"
                        },
                        style = AppTypography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()

        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .padding(paddingNormal.horizontal)
                    .padding(paddingNormal.bottom),
                horizontalArrangement = Arrangement.spacedBy(marginNormal),
                verticalArrangement = Arrangement.spacedBy(marginNormal)
            ) {

                item(span = {
                    GridItemSpan(3)
                }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        // To field
                        OutlinedTextField(
                            value = viewModel.to,
                            onValueChange = viewModel::onToValueChange,
                            label = { Text("To") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_person_svgrepo_com),
                                    contentDescription = null
                                )
                            }
                        )
                        Spacer(modifier = Modifier.size(marginMinimal))
                        // Amount field
                        OutlinedTextField(
                            value = viewModel.amount,
                            onValueChange = viewModel::onAmountValueChange,
                            label = { Text("Amount") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_rupee),
                                    contentDescription = null
                                )
                            }
                        )
                        Spacer(modifier = Modifier.size(marginMinimal))
                        // Date field
                        OutlinedTextField(
                            value = selectedDate,
                            onValueChange = { },
                            label = { Text("Date") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        showDatePicker = true
                                    }
                                }
                                .focusable(interactionSource = interactionSource),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_rupee),
                                    contentDescription = null
                                )
                            },
                            readOnly = true,
                            interactionSource = interactionSource
                        )
                        Spacer(modifier = Modifier.size(marginMinimal))
                        // Note field
                        OutlinedTextField(
                            value = viewModel.note,
                            onValueChange = viewModel::onNoteValueChange,
                            label = { Text("Note") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            minLines = 5,
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(paddingNormal.vertical),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_notes),
                                        contentDescription = null
                                    )
                                }

                            }
                        )
                        Spacer(modifier = Modifier.size(marginNormal))

                        Row(
                            modifier = Modifier.selectableGroup(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TransactionStatus.entries.forEach { type ->
                                RadioButton(
                                    selected = viewModel.selectedOption == type,
                                    onClick = { viewModel.onTransactionStatusValueChange(type) })
                                Text(
                                    text = type.name.toTitleCase(),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(marginNormal))
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerLow,
                                RoundedCornerShape(5)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = {
                                    launchPhotoPicker()
                                },
                                modifier = Modifier,
                                colors = IconButtonDefaults.iconButtonColors()
                                    .copy(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.round_add_circle_outline_24),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text("Add Images")
                        }

                    }

                }

                items(selectedImages.toList()) { imageUri ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize()
                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Box(modifier = Modifier.padding(paddingMinimal.all)) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        MaterialTheme.colorScheme.surface,
                                        CircleShape
                                    )
                                    .clickable {
                                        viewModel.removeSelectedBillImage(imageUri)
                                    }
                            )
                        }
                    }
                }
            }

            // Fixed Save Button at the bottom
            Button(
                onClick = {
                    viewModel.addTransaction(planId = args.planId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingNormal.all)
            ) {
                Text(stringResource(R.string.save))
            }

        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                    focusManager.clearFocus()
                },
                confirmButton = {
                    Button(onClick = {
                        showDatePicker = false
                        focusManager.clearFocus()
                    }) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDatePicker = false
                    }) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }

}
