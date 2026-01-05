private fun fetchSheetData(colIndex: Int) {
    val today = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date())
    val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

    OkHttpClient().newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            val lines = response.body?.string()?.split("\n") ?: return
            for (line in lines) {
                val row = line.split(",")
                if (row.isNotEmpty() && row[0].trim() == today) {
                    val rawPanchang = row.getOrNull(colIndex) ?: ""
                    val dayName = row.getOrNull(28) ?: ""
                    val rawFestival = row.getOrNull(30) ?: "" // તહેવારની કોલમ
                    
                    // તિથિ અને તહેવાર બંનેને ભેગા કરીને ટ્રાન્સલેટ કરો
                    val textToTranslate = "$rawPanchang, $dayName | $rawFestival"
                    
                    currentTranslator?.translate(textToTranslate)?.addOnSuccessListener { translated ->
                        val parts = translated.split("|")
                        val translatedPanchang = applyCorrections(parts.getOrNull(0) ?: "")
                        val translatedFestival = parts.getOrNull(1) ?: ""

                        runOnUiThread {
                            txtDate.text = "${row[0]}/2026"
                            txtPanchang.text = translatedPanchang
                            txtFestival.text = translatedFestival.trim()
                            txtEmoji.text = row.getOrNull(31) ?: ""
                        }
                    }
                    break
                }
            }
        }
        override fun onFailure(call: Call, e: IOException) {}
    })
}

// ગૂગલની ભૂલો સુધારવા માટેનું ફંક્શન
private fun applyCorrections(input: String): String {
    var text = input
    val corrections = mapOf(
        "વાડ" to "વદ",
        "સુદિ" to "સુદ",
        "એકમ" to "પડવો", // જો તમારે પડવો રાખવો હોય તો
        "બીજ" to "દુિતયા"
    )
    corrections.forEach { (wrong, right) ->
        text = text.replace(wrong, right)
    }
    return text
}

