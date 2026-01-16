package com.example.art.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.art.utils.ClipboardUtils
import com.example.art.viewmodel.ZoomTemplateViewModel

/**
 * Main screen for Zoom Class Template application
 * Features: Template editing, Zoom link input, preview, and clipboard copy
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoomTemplateScreen(
    viewModel: ZoomTemplateViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    // Dialog states
    var showTemplateEditor by remember { mutableStateOf(false) }
    var showPreviewEditor by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Zoom Class Template",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val success = ClipboardUtils.copyToClipboard(
                        context,
                        viewModel.getMessageForCopy()
                    )
                    if (success) {
                        Toast.makeText(
                            context,
                            "Message copied to clipboard!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to copy message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                icon = { Icon(Icons.Default.ContentCopy, contentDescription = "Copy") },
                text = { Text("Copy Message") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()  // Add padding when keyboard is visible
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Zoom Link Input Section
            ZoomLinkInputSection(
                zoomLink = viewModel.zoomLink,
                onZoomLinkChange = { viewModel.updateZoomLink(it) }
            )
            
            // Base Template Section
            TemplateSection(
                template = viewModel.baseTemplate,
                onEditClick = { showTemplateEditor = true }
            )
            
            // Generated Preview Section
            GeneratedPreviewSection(
                generatedMessage = viewModel.generatedMessage,
                isManuallyEdited = viewModel.isManuallyEdited,
                onEditClick = { showPreviewEditor = true },
                onResetClick = { viewModel.resetToTemplate() }
            )
            
            // Spacer for FAB
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
    
    // Template Editor Dialog
    if (showTemplateEditor) {
        TextEditorDialog(
            title = "Edit Template",
            initialText = viewModel.baseTemplate,
            onDismiss = { showTemplateEditor = false },
            onSave = { newTemplate ->
                viewModel.updateBaseTemplate(newTemplate)
                showTemplateEditor = false
            }
        )
    }
    
    // Preview Editor Dialog
    if (showPreviewEditor) {
        TextEditorDialog(
            title = "Edit Preview",
            initialText = viewModel.generatedMessage,
            onDismiss = { showPreviewEditor = false },
            onSave = { newMessage ->
                viewModel.updateGeneratedMessage(newMessage)
                showPreviewEditor = false
            }
        )
    }
}

/**
 * Zoom Link Input Section
 */
@Composable
fun ZoomLinkInputSection(
    zoomLink: String,
    onZoomLinkChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Zoom Link",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            OutlinedTextField(
                value = zoomLink,
                onValueChange = onZoomLinkChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter Zoom meeting link") },
                placeholder = { Text("https://zoom.us/j/...") },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Link,
                        contentDescription = "Link",
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

/**
 * Template Section
 */
@Composable
fun TemplateSection(
    template: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Base Template",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                FilledTonalButton(
                    onClick = onEditClick,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }
            }
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = template,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Generated Preview Section
 */
@Composable
fun GeneratedPreviewSection(
    generatedMessage: String,
    isManuallyEdited: Boolean,
    onEditClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Generated Preview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    if (isManuallyEdited) {
                        Text(
                            text = "Manually edited",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isManuallyEdited) {
                        FilledTonalIconButton(onClick = onResetClick) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Reset to template"
                            )
                        }
                    }
                    
                    FilledTonalButton(
                        onClick = onEditClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Edit")
                    }
                }
            }
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = generatedMessage,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Text Editor Dialog for editing template or preview
 */
@Composable
fun TextEditorDialog(
    title: String,
    initialText: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var editedText by remember { mutableStateOf(initialText) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            OutlinedTextField(
                value = editedText,
                onValueChange = { editedText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 400.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        confirmButton = {
            Button(onClick = { onSave(editedText) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
