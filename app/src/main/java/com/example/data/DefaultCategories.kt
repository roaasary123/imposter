package com.example.data

import com.example.model.CategoryItem
import com.example.model.SecretWordItem

object DefaultCategories {

    val BUILTIN_CATEGORIES = listOf(
        CategoryItem("foods", "أطعمة ومأكولات", "restaurant", isCustom = false, wordCount = 12),
        CategoryItem("cities", "مدن ودول", "flight", isCustom = false, wordCount = 12),
        CategoryItem("animals", "حيوانات وكائنات", "pets", isCustom = false, wordCount = 12),
        CategoryItem("sports", "رياضة وألعاب", "sports", isCustom = false, wordCount = 10),
        CategoryItem("jobs", "مهن ووظائف", "work", isCustom = false, wordCount = 12),
        CategoryItem("tech", "تقنية وأجهزة", "phone", isCustom = false, wordCount = 10),
        CategoryItem("home", "أدوات منزلية", "home", isCustom = false, wordCount = 10),
        CategoryItem("transport", "وسائل نقل", "car", isCustom = false, wordCount = 10),
        CategoryItem("entertainment", "ترفيه وسينما", "movie", isCustom = false, wordCount = 8)
    )

    val BUILTIN_WORDS = mapOf(
        "foods" to listOf(
            SecretWordItem("بيتزا", "شرائح", "foods"),
            SecretWordItem("شاورما", "دوران", "foods"),
            SecretWordItem("كبسة", "عزيمة", "foods"),
            SecretWordItem("فلافل", "صباح", "foods"),
            SecretWordItem("برجر", "طبقات", "foods"),
            SecretWordItem("ملوخية", "أخضر", "foods"),
            SecretWordItem("كنافة", "قطر", "foods"),
            SecretWordItem("سوشي", "لفة", "foods"),
            SecretWordItem("معكرونة", "صلصة", "foods"),
            SecretWordItem("حمص", "شامي", "foods"),
            SecretWordItem("فطيرة", "فرن", "foods"),
            SecretWordItem("آيس كريم", "مخروط", "foods")
        ),
        "cities" to listOf(
            SecretWordItem("مكة المكرمة", "قبلة", "cities"),
            SecretWordItem("القاهرة", "نيل", "cities"),
            SecretWordItem("دبي", "أبراج", "cities"),
            SecretWordItem("باريس", "أناقة", "cities"),
            SecretWordItem("طوكيو", "تقنية", "cities"),
            SecretWordItem("القدس", "أسوار", "cities"),
            SecretWordItem("إسطنبول", "مضيق", "cities"),
            SecretWordItem("لندن", "ساعة", "cities"),
            SecretWordItem("الرياض", "عاصمة", "cities"),
            SecretWordItem("المدينة المنورة", "سكينة", "cities"),
            SecretWordItem("بغداد", "دجلة", "cities"),
            SecretWordItem("مسقط", "هدوء", "cities")
        ),
        "animals" to listOf(
            SecretWordItem("أسد", "عرين", "animals"),
            SecretWordItem("صقر", "تحليق", "animals"),
            SecretWordItem("جمل", "صبر", "animals"),
            SecretWordItem("بطريق", "جليد", "animals"),
            SecretWordItem("دلفين", "أمواج", "animals"),
            SecretWordItem("نمر", "خطوط", "animals"),
            SecretWordItem("فهد", "عداء", "animals"),
            SecretWordItem("ثعلب", "مكر", "animals"),
            SecretWordItem("زرافة", "رقبة", "animals"),
            SecretWordItem("بومة", "ليل", "animals"),
            SecretWordItem("فيل", "عاج", "animals"),
            SecretWordItem("حوت", "عمق", "animals")
        ),
        "sports" to listOf(
            SecretWordItem("كرة القدم", "ملعب", "sports"),
            SecretWordItem("كرة السلة", "سلة", "sports"),
            SecretWordItem("التنس", "مضرب", "sports"),
            SecretWordItem("السباحة", "مسبح", "sports"),
            SecretWordItem("الشطرنج", "رقعة", "sports"),
            SecretWordItem("الملاكمة", "حلبة", "sports"),
            SecretWordItem("الجولف", "حفرة", "sports"),
            SecretWordItem("البادل", "جدار", "sports"),
            SecretWordItem("الفروسية", "خيل", "sports"),
            SecretWordItem("الجري", "مسار", "sports")
        ),
        "jobs" to listOf(
            SecretWordItem("طبيب", "سماعة", "jobs"),
            SecretWordItem("مهندس", "مخطط", "jobs"),
            SecretWordItem("معلم", "سبورة", "jobs"),
            SecretWordItem("طيار", "قمرة", "jobs"),
            SecretWordItem("رائد فضاء", "مدار", "jobs"),
            SecretWordItem("طباخ", "نكهة", "jobs"),
            SecretWordItem("محامي", "قانون", "jobs"),
            SecretWordItem("إطفائي", "لهب", "jobs"),
            SecretWordItem("صحفي", "خبر", "jobs"),
            SecretWordItem("مبرمج", "شفرة", "jobs"),
            SecretWordItem("شرطي", "أمن", "jobs"),
            SecretWordItem("مصور", "عدسة", "jobs")
        ),
        "tech" to listOf(
            SecretWordItem("هاتف ذكي", "شاشة", "tech"),
            SecretWordItem("حاسوب محمول", "معالج", "tech"),
            SecretWordItem("سماعات لاسلكية", "عزل", "tech"),
            SecretWordItem("كاميرا", "إطار", "tech"),
            SecretWordItem("بلايستيشن", "ذراع", "tech"),
            SecretWordItem("طابعة", "حبر", "tech"),
            SecretWordItem("شاحن متجول", "طاقة", "tech"),
            SecretWordItem("ساعة ذكية", "معصم", "tech"),
            SecretWordItem("تلفاز ذكي", "بث", "tech"),
            SecretWordItem("نظارة واقع افتراضي", "افتراضي", "tech")
        ),
        "home" to listOf(
            SecretWordItem("ثلاجة", "تبريد", "home"),
            SecretWordItem("مكيف", "حرارة", "home"),
            SecretWordItem("غسالة", "دوران", "home"),
            SecretWordItem("أريكة", "راحة", "home"),
            SecretWordItem("مرآة", "انعكاس", "home"),
            SecretWordItem("وسادة", "نوم", "home"),
            SecretWordItem("مكنسة كهربائية", "غبار", "home"),
            SecretWordItem("ميكروويف", "موجات", "home"),
            SecretWordItem("مصباح طاولة", "إضاءة", "home"),
            SecretWordItem("طاولة طعام", "وجبة", "home")
        ),
        "transport" to listOf(
            SecretWordItem("سيارة رياضية", "محرك", "transport"),
            SecretWordItem("طائرة نفاثة", "تحليق", "transport"),
            SecretWordItem("قطار سريع", "سكة", "transport"),
            SecretWordItem("سفينة كروز", "موج", "transport"),
            SecretWordItem("دراجة نارية", "خوذة", "transport"),
            SecretWordItem("غواصة", "أعماق", "transport"),
            SecretWordItem("حافلة", "مواقف", "transport"),
            SecretWordItem("منطاد", "شعلة", "transport"),
            SecretWordItem("دراجة هوائية", "دواسة", "transport"),
            SecretWordItem("سكوتر كهربائي", "رصيف", "transport")
        ),
        "entertainment" to listOf(
            SecretWordItem("سينما", "فشار", "entertainment"),
            SecretWordItem("مسرحية", "خشبة", "entertainment"),
            SecretWordItem("رسوم متحركة", "رسم", "entertainment"),
            SecretWordItem("وثائقي", "حقيقة", "entertainment"),
            SecretWordItem("رواية بوليسية", "لغز", "entertainment"),
            SecretWordItem("سيرك", "خيمة", "entertainment"),
            SecretWordItem("مهرجان", "احتفال", "entertainment"),
            SecretWordItem("أوبرا", "صوت", "entertainment")
        )
    )
}

