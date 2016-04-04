package me.mathiasluo.page.news.newsutil;

import me.mathiasluo.R;

/**
 * Created by mathiasluo on 16-3-26.
 */
public enum DefaultNewsCategories  {

    academic
            ("Academic", R.drawable.news_ic_academic, "http://www.purdue.edu/newsroom/rss/academics.xml", "academics"),
    agriculture
            ("Agriculture", R.drawable.news_ic_agriculture, "http://www.purdue.edu/newsroom/rss/AgriNews.xml", "Agri"),
    business
            ("Business", R.drawable.news_ic_business, "http://www.purdue.edu/newsroom/rss/BizNews.xml", "Biz"),
    community
            ("Community", R.drawable.news_ic_community, "http://www.purdue.edu/newsroom/rss/community.xml", "community"),
    diversity
            ("Diversity", R.drawable.news_ic_diversity, "http://www.purdue.edu/newsroom/rss/DiversityNews.xml", "Diversity"),
    edu_career
            ("Education and Career", R.drawable.news_ic_edu_career, "http://www.purdue.edu/newsroom/rss/EdCareerNews.xml", "EdCareer"),
    events
            ("Events", R.drawable.news_ic_events, "http://www.purdue.edu/newsroom/rss/EventNews.xml", "Event"),
    featured
            ("Featured", R.drawable.news_ic_featured, "http://www.purdue.edu/newsroom/rss/FeaturedNews.xml", "Featured"),
    general
            ("General", R.drawable.news_ic_general, "http://www.purdue.edu/newsroom/rss/general.xml", "general"),
    health
            ("Health and Medicine", R.drawable.news_ic_health_medicine, "http://www.purdue.edu/newsroom/rss/HealthMedNews.xml", "HealthMed"),
    it
            ("Information Technology", R.drawable.news_ic_it, "http://www.purdue.edu/newsroom/rss/InfoTech.xml", "InfoTech"),
    lifestyle
            ("Lifestyle", R.drawable.news_ic_lifestyle, "http://www.purdue.edu/newsroom/rss/LifeNews.xml", "LifeNews"),
    lifesci
            ("Life Sciences", R.drawable.news_ic_life_sciences, "http://www.purdue.edu/newsroom/rss/LifeSciNews.xml", "LifeSci"),
    outreach
            ("Outreach", R.drawable.news_ic_outreach, "http://www.purdue.edu/newsroom/rss/outreach.xml", "outreach"),
    physicalsci
            ("Physical Sciences", R.drawable.news_ic_physical_sciences, "http://www.purdue.edu/newsroom/rss/PhysicalSciNews.xml", "PhysicalSci"),
    research
            ("Research Foundation", R.drawable.news_ic_research, "http://www.purdue.edu/newsroom/rss/ResearchNews.xml", "Research"),
    student
            ("Student", R.drawable.news_ic_student, "http://www.purdue.edu/newsroom/rss/StudentNews.xml", "StudentNews"),
    vetmed
            ("Veterinary Medicine", R.drawable.news_ic_vet_med, "http://www.purdue.edu/newsroom/rss/VetMedNews.xml", "VetMed");

    private String printable;
    private int drawableResource;
    private String url;
    private String title;

    DefaultNewsCategories(String printable, int drawableResource, String url, String title) {
        this.printable = printable;
        this.drawableResource = drawableResource;
        this.url = url;
        this.title = title;
    }

    public String printable() {
        return printable;
    }

    public int drawableResource() {
        return drawableResource;
    }

    public String url() {
        return url;
    }

    public String title() {
        return title;
    }

}

