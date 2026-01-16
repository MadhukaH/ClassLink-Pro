package com.example.art.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ViewModel for managing Zoom class template state and logic
 * Implements MVVM pattern for clean architecture
 */
class ZoomTemplateViewModel : ViewModel() {
    
    // Default template with Sinhala text
    var baseTemplate by mutableStateOf(
        """🧑‍🏫 ගුරුතුමිය: නිලක්ෂි හෙට්ටිආරච්චි
🎨 Colour Zone Online Art Class 
📍 ස්ථානය: සූරියවැව
💻 මාධ්‍යය: Zoom

🔗 Zoom ලින්ක්: {{ZOOM_LINK}}

කරුණාකර නියමිත වේලාවට Zoom ලින්ක් එක භාවිතා කර පන්තියට සම්බන්ධ වන්න. පන්තිය ආරම්භයට පෙර ඔබගේ අන්තර්ජාල සම්බන්ධතාවය, ශබ්දය සහ කැමරාව පරීක්ෂා කර ගන්න."""
    )
        private set
    
    // User input for Zoom link
    var zoomLink by mutableStateOf("")
        private set
    
    // Generated message (template + zoom link)
    var generatedMessage by mutableStateOf(baseTemplate)
        private set
    
    // Flag to track if user has manually edited the generated message
    var isManuallyEdited by mutableStateOf(false)
        private set
    
    /**
     * Update the base template
     */
    fun updateBaseTemplate(newTemplate: String) {
        baseTemplate = newTemplate
        // Regenerate message if not manually edited
        if (!isManuallyEdited) {
            regenerateMessage()
        }
    }
    
    /**
     * Update Zoom link and regenerate message
     */
    fun updateZoomLink(newLink: String) {
        zoomLink = newLink
        // Reset manual edit flag when link changes
        isManuallyEdited = false
        regenerateMessage()
    }
    
    /**
     * Update generated message directly (manual edit)
     */
    fun updateGeneratedMessage(newMessage: String) {
        generatedMessage = newMessage
        isManuallyEdited = true
    }
    
    /**
     * Reset manual edit flag and regenerate from template
     */
    fun resetToTemplate() {
        isManuallyEdited = false
        regenerateMessage()
    }
    
    /**
     * Generate message by replacing placeholder with Zoom link
     */
    private fun regenerateMessage() {
        generatedMessage = if (zoomLink.isNotBlank()) {
            baseTemplate.replace("{{ZOOM_LINK}}", zoomLink)
        } else {
            baseTemplate
        }
    }
    
    /**
     * Validate if Zoom link is provided
     */
    fun isZoomLinkValid(): Boolean {
        return zoomLink.isNotBlank()
    }
    
    /**
     * Get current message for copying
     */
    fun getMessageForCopy(): String {
        return generatedMessage
    }
}
