package com.example.workcareer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView rvJobListings;
    private JobListingsAdapter adapter;
    private List<JobListing> jobListings;
    private JobListingDao jobListingDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rvJobListings = view.findViewById(R.id.rv_job_listings);
        adapter = new JobListingsAdapter(getJobListingDao(requireContext()), requireActivity());
        adapter.setOnJobClickListener((jobListing, isLiked) -> {
            if (isLiked) {
                addToFavorites(jobListing);
            } else {
                removeFromFavorites(jobListing);
            }
        });
        rvJobListings.setAdapter(adapter);
        rvJobListings.setLayoutManager(new LinearLayoutManager(getContext()));

        jobListings = getJobListingsFromApi();
        adapter.setJobListings(jobListings);

        return view;
    }

    private List<JobListing> getJobListingsFromApi() {
        List<JobListing> jobListings = new ArrayList<>();
        JobListing job1 = new JobListing("Разработчик программного обеспечения", "Google", "Маунтин-Вью, Калифорния");
        JobListing job2 = new JobListing("Специалист по данным", "Amazon", "Сиэтл, Вашингтон");
        JobListing job3 = new JobListing("Менеджер продукта", "Facebook", "Менло-Парк, Калифорния");
        JobListing job4 = new JobListing("Инженер по кибербезопасности", "Microsoft", "Редмонд, Вашингтон");
        JobListing job5 = new JobListing("Специалист по искусственному интеллекту", "Apple", "Купертино, Калифорния");
        JobListing job6 = new JobListing("Веб-разработчик", "Netflix", "Лос-Гатос, Калифорния");
        JobListing job7 = new JobListing("Финансовый аналитик", "JPMorgan Chase", "Нью-Йорк, Нью-Йорк");
        JobListing job8 = new JobListing("Медицинский работник", "Mayo Clinic", "Рочестер, Миннесота");
        JobListing job9 = new JobListing("Юрист", "Latham & Watkins", "Лос-Анджелес, Калифорния");
        JobListing job10 = new JobListing("Бухгалтер", "Deloitte", "Нью-Йорк, Нью-Йорк");
        JobListing job11 = new JobListing("Маркетолог", "Coca-Cola", "Атланта, Джорджия");
        JobListing job12 = new JobListing("Психолог", "Stanford University", "Стэнфорд, Калифорния");
        JobListing job13 = new JobListing("Архитектор", "Skidmore, Owings & Merrill", "Чикаго, Иллинойс");
        JobListing job14 = new JobListing("Инженер-строитель", "Bechtel", "Сан-Франциско, Калифорния");
        JobListing job15 = new JobListing("Пилот", "Delta Air Lines", "Атланта, Джорджия");
        JobListing job16 = new JobListing("Учитель", "Los Angeles Unified School District", "Лос-Анджелес, Калифорния");
        JobListing job17 = new JobListing("Художник", "Pixar Animation Studios", "Эмервилл, Калифорния");
        JobListing job18 = new JobListing("Фотограф", "National Geographic", "Вашингтон, округ Колумбия");
        JobListing job19 = new JobListing("Ветеринар", "VCA Animal Hospitals", "Лос-Анджелес, Калифорния");
        JobListing job20 = new JobListing("Повар", "The French Laundry", "Йонтвилл, Калифорния");
        JobListing job21 = new JobListing("Дизайнер интерфейсов", "Uber", "Сан-Франциско, Калифорния");
        JobListing job22 = new JobListing("Биоинженер", "Genentech", "Саут-Сан-Франциско, Калифорния");
        JobListing job23 = new JobListing("Актуарий", "Prudential", "Ньюарк, Нью-Джерси");
        JobListing job24 = new JobListing("Редактор", "The New York Times", "Нью-Йорк, Нью-Йорк");
        JobListing job25 = new JobListing("Специалист по управлению цепочками поставок", "Walmart", "Бентонвилль, Арканзас");
        JobListing job26 = new JobListing("Социолог", "University of Chicago", "Чикаго, Иллинойс");
        JobListing job27 = new JobListing("Физик", "Lawrence Berkeley National Laboratory", "Беркли, Калифорния");
        JobListing job28 = new JobListing("Специалист по связям с общественностью", "Edelman", "Чикаго, Иллинойс");
        JobListing job29 = new JobListing("Политолог", "RAND Corporation", "Санта-Моника, Калифорния");
        JobListing job30 = new JobListing("Экономист", "The World Bank", "Вашингтон, округ Колумбия");
        JobListing job31 = new JobListing("Геолог", "ExxonMobil", "Ирвинг, Техас");
        JobListing job32 = new JobListing("Менеджер по персоналу", "Google", "Маунтин-Вью, Калифорния");
        JobListing job33 = new JobListing("Библиотекарь", "New York Public Library", "Нью-Йорк, Нью-Йорк");
        JobListing job34 = new JobListing("Сценарист", "Warner Bros.", "Бербанк, Калифорния");
        JobListing job35 = new JobListing("Композитор", "Sony Music Entertainment", "Нью-Йорк, Нью-Йорк");
        JobListing job36 = new JobListing("Хореограф", "Alvin Ailey American Dance Theater", "Нью-Йорк, Нью-Йорк");
        JobListing job37 = new JobListing("Дипломат", "Государственный департамент США", "Вашингтон, округ Колумбия");
        JobListing job38 = new JobListing("Лингвист", "Middlebury College", "Миддлбери, Вермонт");
        JobListing job39 = new JobListing("Антрополог", "American Museum of Natural History", "Нью-Йорк, Нью-Йорк");
        JobListing job40 = new JobListing("Криминалист", "Federal Bureau of Investigation (FBI)", "Вашингтон, округ Колумбия");
        JobListing job41 = new JobListing("Археолог", "Smithsonian Institution", "Вашингтон, округ Колумбия");
        JobListing job42 = new JobListing("Историк", "Harvard University", "Кембридж, Массачусетс");
        JobListing job43 = new JobListing("Географ", "National Geographic Society", "Вашингтон, округ Колумбия");
        JobListing job44 = new JobListing("Океанограф", "Scripps Institution of Oceanography", "Ла-Хойя, Калифорния");
        JobListing job45 = new JobListing("Метеоролог", "National Weather Service", "Силвер-Спринг, Мэриленд");
        JobListing job46 = new JobListing("Астроном", "NASA", "Вашингтон, округ Колумбия");
        JobListing job47 = new JobListing("Военный аналитик", "RAND Corporation", "Санта-Моника, Калифорния");
        JobListing job48 = new JobListing("Политический консультант", "Gerstein Bocian Agne Strategies", "Вашингтон, округ Колумбия");
        JobListing job49 = new JobListing("Менеджер проектов", "Bain & Company", "Бостон, Массачусетс");
        JobListing job50 = new JobListing("Специалист по логистике", "FedEx", "Мемфис, Теннесси");

        // Добавьте еще 80 профессий здесь

        jobListings.add(job1);
        jobListings.add(job2);
        jobListings.add(job3);
        jobListings.add(job4);
        jobListings.add(job5);
        jobListings.add(job6);
        jobListings.add(job7);
        jobListings.add(job8);
        jobListings.add(job9);
        jobListings.add(job10);
        jobListings.add(job11);
        jobListings.add(job12);
        jobListings.add(job13);
        jobListings.add(job14);
        jobListings.add(job15);
        jobListings.add(job16);
        jobListings.add(job17);
        jobListings.add(job18);
        jobListings.add(job19);
        jobListings.add(job20);
        jobListings.add(job21);
        jobListings.add(job22);
        jobListings.add(job23);
        jobListings.add(job24);
        jobListings.add(job25);
        jobListings.add(job26);
        jobListings.add(job27);
        jobListings.add(job28);
        jobListings.add(job29);
        jobListings.add(job30);
        jobListings.add(job31);
        jobListings.add(job32);
        jobListings.add(job33);
        jobListings.add(job34);
        jobListings.add(job35);
        jobListings.add(job36);
        jobListings.add(job37);
        jobListings.add(job38);
        jobListings.add(job39);
        jobListings.add(job40);
        jobListings.add(job41);
        jobListings.add(job42);
        jobListings.add(job43);
        jobListings.add(job44);
        jobListings.add(job45);
        jobListings.add(job46);
        jobListings.add(job47);
        jobListings.add(job48);
        jobListings.add(job49);
        jobListings.add(job50);

        return jobListings;
    }


    private void addToFavorites(JobListing jobListing) {
        // Операции с базой данных уже выполняются в отдельном потоке в JobListingsAdapter
    }

    private void removeFromFavorites(JobListing jobListing) {
        // Операции с базой данных уже выполняются в отдельном потоке в JobListingsAdapter
    }

    private JobListingDao getJobListingDao( Context context) {
        if (jobListingDao == null) {
            AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "job-listings-db").build();
            jobListingDao = db.jobListingDao();
        }
        return jobListingDao;
    }
}

