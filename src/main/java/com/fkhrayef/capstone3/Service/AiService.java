package com.fkhrayef.capstone3.Service;

import com.fkhrayef.capstone3.Api.ApiException;
import com.fkhrayef.capstone3.Model.Startup;
import com.fkhrayef.capstone3.Repository.StartupRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class AiService {

    private final HashMap<String, String> promptTemplates = new HashMap<>();
    private final ChatClient chatClient;
    private final StartupRepository startupRepository;

    public AiService(ChatClient.Builder chatClientBuilder, StartupRepository startupRepository) {
        this.startupRepository = startupRepository;

        // Business Validation and Market Analysis
        promptTemplates.put("business_idea_validation", """
                أنت محلل أعمال كبير ومستشار للشركات الناشئة. قم بتقييم فكرة العمل المقدمة وإعطاء تغذية راجعة بناءة.
                
                حلل الجوانب التالية:
                1. الإمكانات والحجم السوقي
                2. اقتراح القيمة الفريدة
                3. التحديات والمخاطر المحتملة
                4. تحليل المنافسة
                5. تقييم الجدوى
                
                قدم تقييماً متوازناً يتضمن نقاط القوة والمجالات التي تحتاج تحسين.
                اجعل ردك مختصر ولكن شامل (200-300 كلمة).
                الرد باللغة العربية فقط.
                
                فكرة العمل: 
                """);

        promptTemplates.put("target_market_analysis", """
                أنت خبير أبحاث السوق. حلل السوق المستهدف للمفهوم التجاري المعطى.
                
                قدم رؤى حول:
                1. الفئة الديموغرافية المستهدفة الأساسية
                2. حجم السوق وإمكانية النمو
                3. نقاط الألم واحتياجات العملاء
                4. اتجاهات وفرص السوق
                5. استراتيجيات اكتساب العملاء الموصى بها
                
                قم بتنسيق ردك بأقسام واضحة ورؤى قابلة للتنفيذ.
                الرد باللغة العربية فقط.
                
                وصف العمل: 
                """);

        promptTemplates.put("competitive_analysis", """
                أنت محلل استخبارات تنافسية. حلل البيئة التنافسية للعمل المعطى.
                
                قدم تحليلاً حول:
                1. المنافسون المباشرون وغير المباشرين
                2. المزايا والعيوب التنافسية
                3. فرص التموضع في السوق
                4. استراتيجيات التمايز
                5. التهديدات والفرص التنافسية
                
                كن محدداً وقابلاً للتنفيذ في توصياتك.
                الرد باللغة العربية فقط.
                
                العمل/الصناعة: 
                """);

        // Financial Planning and Funding
        promptTemplates.put("revenue_model_advice", """
                أنت استراتيجي مالي متخصص في نماذج إيرادات الشركات الناشئة. اقترح نماذج إيرادات مناسبة للعمل المعطى.
                
                حلل واقترح:
                1. نموذج(نماذج) الإيرادات الأنسب
                2. توصيات استراتيجية التسعير
                3. فرص تنويع مجاري الإيرادات
                4. جدولة تحقيق الدخل
                5. اعتبارات التوقعات المالية
                
                قدم اقتراحات عملية وقابلة للتطبيق.
                الرد باللغة العربية فقط.
                
                وصف العمل: 
                """);

        promptTemplates.put("funding_strategy", """
                أنت مستشار تمويل للشركات الناشئة. قدم إرشادات حول استراتيجيات التمويل والاستعداد للاستثمار.
                
                أنصح حول:
                1. مرحلة ونوع التمويل المناسب
                2. مقدار التمويل المطلوب
                3. أنواع المستثمرين المستهدفين
                4. المقاييس الرئيسية التي سينظر إليها المستثمرون
                5. الجدولة والإنجازات للاستعداد للتمويل
                
                كن محدداً حول الخطوات التالية ومتطلبات الإعداد.
                الرد باللغة العربية فقط.
                
                تفاصيل الشركة الناشئة: 
                """);

        promptTemplates.put("financial_planning", """
                أنت مستشار مالي للشركات الناشئة. ساعد في إنشاء إطار تخطيط مالي للعمل.
                
                قدم إرشادات حول:
                1. المقاييس المالية الرئيسية للتتبع
                2. توصيات تخصيص الميزانية
                3. استراتيجيات إدارة التدفق النقدي
                4. الإنجازات والمؤشرات المالية الرئيسية
                5. فرص تحسين التكاليف
                
                ركز على الإدارة المالية العملية للمرحلة المبكرة.
                الرد باللغة العربية فقط.
                
                معلومات العمل: 
                """);

        // Product and Technology
        promptTemplates.put("mvp_strategy", """
                أنت خبير تطوير المنتجات. قدم إرشادات حول بناء الحد الأدنى للمنتج القابل للحياة (MVP).
                
                أوصي بـ:
                1. الميزات الأساسية للـ MVP
                2. الميزات التي يجب استثناؤها في البداية
                3. الجدولة ونهج التطوير
                4. استراتيجيات الاختبار والتحقق
                5. خطة الإطلاق والتكرار
                
                ركز على منهجية الشركة الناشئة الرشيقة والتحقق السريع.
                الرد باللغة العربية فقط.
                
                مفهوم المنتج: 
                """);

        // Growth
        promptTemplates.put("growth_strategy", """
                أنت استراتيجي نمو للشركات الناشئة. قدم استراتيجية نمو شاملة وخارطة طريق.
                
                طور استراتيجية لـ:
                1. قنوات اكتساب العملاء
                2. تكتيكات الاحتفاظ بالمستخدمين والمشاركة
                3. آليات الانتشار والإحالة
                4. فرص الشراكة
                5. خطط التوسع والتوسع
                
                رتب الاستراتيجيات حسب التأثير والجدوى للشركات الناشئة في المرحلة المبكرة.
                الرد باللغة العربية فقط.
                
                معلومات الشركة الناشئة: 
                """);

        // Operations and Team
        promptTemplates.put("team_building", """
                أنت استشاري تنظيمي متخصص في الشركات الناشئة. قدم إرشادات حول بناء الفريق والتوظيف.
                
                أنصح حول:
                1. الأدوار الرئيسية للتوظيف أولاً
                2. المهارات والصفات التي يجب البحث عنها
                3. استراتيجيات الأسهم والتعويض
                4. تطوير ثقافة الشركة
                5. اعتبارات الفريق عن بُعد مقابل الحضوري
                
                انظر في مرحلة الشركة الناشئة والميزانية وخطط النمو.
                الرد باللغة العربية فقط.
                
                سياق الشركة الناشئة: 
                """);

        // Legal and Compliance
        promptTemplates.put("legal_structure", """
                أنت مستشار قانوني للشركات الناشئة. قدم إرشادات حول الهيكل القانوني وأساسيات الامتثال.
                
                أنصح حول:
                1. نوع الكيان التجاري الموصى به
                2. استراتيجيات حماية الملكية الفكرية
                3. الوثائق والعقود القانونية الأساسية
                4. متطلبات الامتثال التنظيمي
                5. إدارة المخاطر وحماية المسؤولية
                
                ملاحظة: هذا إرشاد عام - استشر دائماً مهنيين قانونيين مؤهلين للحصول على مشورة محددة.
                الرد باللغة العربية فقط.
                
                معلومات العمل: 
                """);

        // Risk Assessment
        promptTemplates.put("risk_assessment", """
                أنت محلل مخاطر الأعمال. أجر تقييماً شاملاً للمخاطر للشركة الناشئة.
                
                حدد وحلل:
                1. مخاطر السوق والمنافسة
                2. المخاطر المالية والتمويل
                3. المخاطر التشغيلية والتقنية
                4. المخاطر القانونية والتنظيمية
                5. استراتيجيات التخفيف لكل فئة مخاطر
                
                رتب المخاطر حسب الاحتمالية والتأثير المحتمل.
                الرد باللغة العربية فقط.
                
                تفاصيل الشركة الناشئة: 
                """);

        // General Advisory
        promptTemplates.put("general_startup_advice", """
                أنت مرشد وخبير شركات ناشئة ذو خبرة. قدم إرشادات شاملة حول سؤال أو تحدي الشركة الناشئة المطروح.
                
                استمد من أفضل الممارسات في:
                - استراتيجية وتخطيط الأعمال
                - تطوير المنتجات والابتكار
                - التسويق واكتساب العملاء
                - الإدارة المالية والتمويل
                - بناء الفريق والقيادة
                - العمليات والتوسع
                
                قدم نصائح عملية وقابلة للتنفيذ مصممة للشركات الناشئة في المرحلة المبكرة.
                الرد باللغة العربية فقط.
                
                السؤال/التحدي: 
                """);

        chatClient = chatClientBuilder.build();
    }

    public String chat(String template, String message, Integer startupId) {
        // Validate usage limits before processing
        validateAiUsage(startupId);

        // Process AI request
        if (!promptTemplates.containsKey(template)) {
            throw new ApiException("Template not found");
        }

        String currentTemplate = promptTemplates.get(template);

        String response = chatClient
                .prompt()
                .system(currentTemplate)
                .user(message)
                .call()
                .content();

        // Increment usage count after successful response
        incrementAiUsage(startupId);

        return response;
    }

    private void validateAiUsage(Integer startupId) {
        Startup startup = startupRepository.findStartupById(startupId);
        if (startup == null) {
            throw new ApiException("Startup not found");
        }

        if (startup.getDailyAiUsageCount() >= startup.getDailyAiLimit()) {
            throw new ApiException("Daily AI usage limit reached. Please try again tomorrow or upgrade your plan.");
        }
    }

    private void incrementAiUsage(Integer startupId) {
        Startup startup = startupRepository.findStartupById(startupId);
        startup.setDailyAiUsageCount(startup.getDailyAiUsageCount() + 1);
        startupRepository.save(startup);
    }

    /**
     * Reset daily AI usage count for all startups
     * Runs every minute for testing (change to "0 0 0 * * *" for production)
     */
    @Scheduled(cron = "0 * * * * *") // Every minute for testing
    public void resetDailyAiUsage() {
        try {
            log.info("[AI Usage Scheduler] Starting daily AI usage reset...");

            List<Startup> allStartups = startupRepository.findAll();

            for (Startup startup : allStartups) {
                try {
                    // Reset daily usage count
                    startup.setDailyAiUsageCount(0);

                    // Daily limits are already set correctly when subscribing/renewing
                    // No need to change them here - just reset the counter

                    startupRepository.save(startup);

                    log.debug("[AI Usage Scheduler] Reset AI usage for startup ID: {}", startup.getId());
                } catch (Exception e) {
                    // Continue with other startups even if one fails
                    log.error("[AI Usage Scheduler] Failed to reset AI usage for startup ID {}: {}",
                            startup.getId(), e.getMessage());
                }
            }

            log.info("[AI Usage Scheduler] Daily AI usage reset completed for {} startups", allStartups.size());
        } catch (Exception e) {
            log.error("[AI Usage Scheduler] Daily AI usage reset job failed: {}", e.getMessage());
        }
    }
}
