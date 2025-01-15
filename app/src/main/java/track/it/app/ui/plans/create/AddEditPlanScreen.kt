package track.it.app.ui.plans.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import track.it.app.R
import track.it.app.domain.model.Plan
import track.it.app.ui.theme.AppTypography
import track.it.app.ui.theme.marginMinimal
import track.it.app.ui.theme.paddingDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPlanScreen(
    navController: NavController,
    id: Long? = null,
    viewModel: AddEditPlanViewModel = hiltViewModel()
) {
    if (id != null) {
        viewModel.setPlanIdForEdit(id)
        viewModel.editPlan.collectAsStateWithLifecycle()
    }
    LaunchedEffect(viewModel.planResult) {
        if (viewModel.planResult?.isSuccess == true) {
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (id == null) {
                            "Add Plan"
                        } else {
                            "Edit Plan"
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(paddingDefault.horizontal)
                    .padding(paddingDefault.bottom)
                    .verticalScroll(rememberScrollState())
            ) {

                // Title field
                OutlinedTextField(
                    value = viewModel.title,
                    onValueChange = viewModel::onTitleValueChange,
                    label = { Text("Plan Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_new_plan),
                            contentDescription = null
                        )
                    }
                )
                Spacer(modifier = Modifier.size(marginMinimal))
                // Budget field
                OutlinedTextField(
                    value = viewModel.budget,
                    onValueChange = viewModel::onBudgetValueChange,
                    label = { Text("Budget") },
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

                //Description Field
                OutlinedTextField(
                    value = viewModel.descriptions,
                    onValueChange = viewModel::onDescriptionValueChange,
                    label = { Text("Note") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    minLines = 5,
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(paddingDefault.vertical),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_notes),
                                contentDescription = null
                            )
                        }

                    }
                )

                Spacer(modifier = Modifier.weight(1f))

            }

            // Fixed Save Button at the bottom
            Button(
                onClick = {
                    viewModel.addPlans(
                        Plan(
                            title = viewModel.title,
                            description = viewModel.descriptions,
                            initialBudget = viewModel.budget.toDouble()
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingDefault.all)
            ) {
                Text("Save")
            }

        }
    }
}