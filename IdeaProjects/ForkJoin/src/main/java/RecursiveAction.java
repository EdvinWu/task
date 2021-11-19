import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import static java.lang.Thread.sleep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class RecursiveAction extends RecursiveTask<CopyOnWriteArraySet<String>> {

    private final Node<String> rootUrl;
    private final Node<String> childUrl;

    CopyOnWriteArraySet<String> links = new CopyOnWriteArraySet<>();

    public RecursiveAction(Node<String> rootUrl, Node<String> childUrl) {
        this.rootUrl = rootUrl;
        this.childUrl = childUrl;
    }

    @Override
    protected CopyOnWriteArraySet<String> compute() {

        try {
            sleep(350);
            Document doc = Jsoup.connect(rootUrl.getUrl()).timeout(10000).get();
            Elements elements = doc.select("a[href]");

            for (Element el : elements) {
                String url = el.attr("abs:href");
                if (linkFilter(url)) {
                    rootUrl.addChild(new Node<>(url));
                    links.add(url);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Set<RecursiveAction> taskList = new HashSet<>(createTasks());

        taskList.forEach(ForkJoinTask::fork);

//        joinResults(links, taskList);

        return links;
    }

    private Set<RecursiveAction> createTasks() {

        Set<RecursiveAction> forks = new HashSet<>();

        for (Node<String> url : rootUrl.getChildren()) {
            RecursiveAction act = new RecursiveAction(url, rootUrl);
            forks.add(act);
        }
        return forks;
    }

    private void joinResults(Set<String> list, Set<RecursiveAction> tasks) {
        for (RecursiveAction item : tasks) {
            list.addAll(item.join());
        }
    }

    private boolean linkFilter(String url) {
        return (!url.contains("#") && !links.contains(url)
                && !url.endsWith(".pdf"));
    }
}