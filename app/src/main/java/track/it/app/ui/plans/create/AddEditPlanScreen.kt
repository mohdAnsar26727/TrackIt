package track.it.app.ui.plans.create

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import track.it.app.domain.model.BudgetUnit
import track.it.app.domain.model.PlanForm.Budget
import track.it.app.domain.model.PlanForm.Name
import track.it.app.domain.model.PlanForm.Note
import track.it.app.ui.model.PlanFormField
import track.it.app.ui.theme.AppTypography
import track.it.app.ui.theme.paddingMedium
import track.it.app.ui.widget.CancellableSaveActionButton
import track.it.app.ui.widget.FormInputField
import track.it.app.ui.widget.SpacerSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPlanScreen(
    navController: NavController,
    id: Long? = null,
    viewModel: AddEditPlanViewModel = hiltViewModel()
) {
    if (id != null) {
        viewModel.setPlanIdForEdit(id)
    }

    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val planResult by viewModel.planResult.collectAsStateWithLifecycle(null)

    LaunchedEffect(planResult) {
        if (planResult?.isSuccess == true) {
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
                    .animateContentSize()
                    .padding(paddingMedium.horizontal)
                    .padding(paddingMedium.bottom)
                    .verticalScroll(rememberScrollState())
            ) {

                PlanFormField.fields.forEach { (field, config) ->

                    val title: String
                    val callBack: (String) -> Unit


                    when (field) {
                        Name -> {
                            title = formState.title
                            callBack = viewModel::onTitleValueChange
                        }

                        Budget -> {
                            title = formState.budget
                            callBack = viewModel::onBudgetValueChange
                        }

                        Note -> {
                            title = formState.description
                            callBack = viewModel::onDescriptionValueChange
                        }
                    }

                    SpacerSmall()

                    FormInputField(
                        config = config,
                        value = title,
                        onValueChange = callBack,
                        validation = formState.validationResult[field],
                        trailingContent = {
                            if (field == Budget) {
                                UnitSelector(
                                    selectedUnit = formState.selectedBudgetUnit,
                                    onUnitChange = viewModel::onBudgetUnitChange
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            // Fixed Save Button at the bottom
            CancellableSaveActionButton(
                onPrimaryAction = {
                    viewModel.addPlan(id)
                },
                onSecondaryAction = {
                    navController.navigateUp()
                }
            )
        }
    }
}

@Composable
fun UnitSelector(
    selectedUnit: BudgetUnit,
    onUnitChange: (BudgetUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp)
        ) {
            Text(
                selectedUnit.displayName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select unit")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            border = BorderStroke(
                OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor
            )
        ) {
            BudgetUnit.entries.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit.displayName, style = MaterialTheme.typography.titleSmall) },
                    onClick = {
                        onUnitChange(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

