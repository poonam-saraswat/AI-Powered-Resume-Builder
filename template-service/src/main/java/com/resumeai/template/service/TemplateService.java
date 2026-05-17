package com.resumeai.template.service;

import com.resumeai.template.entity.Template;
import com.resumeai.template.repository.TemplateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository repo;

    public List<Template> listByUser(UUID userId){ return repo.findByUserId(userId); }
    public List<Template> listSamples(){ return repo.findByIsSampleTrue(); }
    public Template get(UUID id){ return repo.findById(id).orElseThrow(); }
    public Template create(Template e){ return repo.save(e); }
    public Template update(UUID id, Template e){ e.setId(id); return repo.save(e); }
    public void delete(UUID id){ repo.deleteById(id); }

    @PostConstruct
    public void seedSamples() {
        if (!repo.findByIsSampleTrue().isEmpty()) return;

        repo.saveAll(List.of(
                sample("Modern Professional", "Professional",
                        "https://images.unsplash.com/photo-1586281380349-632531db7ed4?w=400",
                        """
                        <div style="font-family:Inter,sans-serif;padding:40px;color:#111">
                          <h1 style="margin:0;font-size:32px">{{fullName}}</h1>
                          <p style="color:#555;margin:4px 0 16px">{{title}} · {{email}} · {{phone}}</p>
                          <hr/>
                          <h2 style="color:#2563eb">Summary</h2><p>{{summary}}</p>
                          <h2 style="color:#2563eb">Experience</h2><div>{{experience}}</div>
                          <h2 style="color:#2563eb">Education</h2><div>{{education}}</div>
                          <h2 style="color:#2563eb">Skills</h2><div>{{skills}}</div>
                        </div>"""),
                sample("Classic Serif", "Traditional",
                        "https://images.unsplash.com/photo-1606857521015-7f9fcf423740?w=400",
                        "<div style='font-family:Georgia,serif;padding:48px'><h1 style='text-align:center'>{{fullName}}</h1><p style='text-align:center'>{{email}} | {{phone}}</p><hr/><h3>Summary</h3><p>{{summary}}</p><h3>Experience</h3>{{experience}}<h3>Education</h3>{{education}}<h3>Skills</h3>{{skills}}</div>"),
                sample("Creative Bold", "Creative",
                        "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?w=400",
                        "<div style='font-family:Poppins,sans-serif;display:grid;grid-template-columns:1fr 2fr;min-height:100vh'><aside style='background:#7c3aed;color:#fff;padding:32px'><h1>{{fullName}}</h1><p>{{title}}</p><p>{{email}}</p><p>{{phone}}</p><h3>Skills</h3>{{skills}}</aside><main style='padding:32px'><h2>About</h2><p>{{summary}}</p><h2>Experience</h2>{{experience}}<h2>Education</h2>{{education}}</main></div>"),
                sample("Minimal Mono", "Minimal",
                        "https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=400",
                        "<div style='font-family:JetBrains Mono,monospace;padding:40px;max-width:780px;margin:auto'><h1 style='border-bottom:2px solid #000'>{{fullName}}</h1><p>{{email}} · {{phone}}</p><h2># summary</h2><p>{{summary}}</p><h2># experience</h2>{{experience}}<h2># education</h2>{{education}}<h2># skills</h2>{{skills}}</div>"),
                sample("Tech Sidebar", "Tech",
                        "https://images.unsplash.com/photo-1573164713714-d95e436ab8d6?w=400",
                        "<div style='font-family:Inter,sans-serif;display:flex'><div style='background:#0f172a;color:#fff;padding:32px;width:33%'><h1>{{fullName}}</h1><p>{{title}}</p><h3>Contact</h3><p>{{email}}</p><p>{{phone}}</p><h3>Skills</h3>{{skills}}</div><div style='padding:32px;flex:1'><h2>Summary</h2><p>{{summary}}</p><h2>Experience</h2>{{experience}}<h2>Education</h2>{{education}}</div></div>"),
                sample("Executive Elite", "Executive",
                        "https://images.unsplash.com/photo-1664575602554-2087b04935a5?w=400",
                        "<div style='font-family:Garamond,serif;padding:56px;max-width:820px;margin:auto'><h1 style='font-size:40px;letter-spacing:2px;text-align:center'>{{fullName}}</h1><p style='text-align:center;color:#666'>{{title}}</p><p style='text-align:center'>{{email}} • {{phone}}</p><hr style='border-color:#c9a84c'/><h2 style='color:#c9a84c'>Executive Summary</h2><p>{{summary}}</p><h2 style='color:#c9a84c'>Experience</h2>{{experience}}<h2 style='color:#c9a84c'>Education</h2>{{education}}<h2 style='color:#c9a84c'>Core Competencies</h2>{{skills}}</div>")
        ));
    }

    private Template sample(String name, String category, String preview, String html){
        Template t = new Template();
        t.setName(name);
        t.setCategory(category);
        t.setPreviewUrl(preview);
        t.setHtmlTemplate(html);
        t.setSample(true);
        return t;
    }
}
