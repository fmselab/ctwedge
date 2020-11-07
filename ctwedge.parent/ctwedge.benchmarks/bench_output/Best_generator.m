%%  Clear
clear
clc

%% Import dati

data = readtable('benchmark_03112020_202725.csv');
data = removevars(data, 'Var12');



%%  Calcolo migliori gen. per #test

col_test = {};
col_names = data.Properties.VariableNames;

for name = col_names
    char_name = char(name);
    if (contains(char_name, 'test'))
        col_test{end+1} = char_name;
    end
end

gen_names = [];
for name = col_test
    gen_names = [gen_names, strtok(name, '_')];
end

test_only = data(:,col_test);
best_test = [];

for iter = 1:size(test_only, 1)
   row = test_only(iter,:);
   array = table2array(row);
   array2 = str2double(array);
   minimo = min(array2);
   idx = array2 == minimo;
   best_test = [best_test; idx];
end

sum_best_test = [];
for i = 1:size(best_test,2)
    sum_best_test = [sum_best_test, sum(best_test(:,i))];
end    

%%  Calcolo medie gen. per #test


mean_test = [];

for iter = 1:size(test_only, 1)
   row = test_only(iter,:);
   array = table2array(row);
   array2 = str2double(array);
   media = mean(array2, 'omitnan');
   res = array2 / media;
   mean_test = [mean_test; array2 / media];
end

mean_mean_test = [];
for i = 1:size(mean_test,2)
    mean_mean_test = [mean_mean_test, mean(mean_test(:,i), 'omitnan')];
end  

mean_mean_test = (mean_mean_test - 1) * -100;

%%  Calcolo migliori gen. per tempo di esecuzione

col_times = {};

for name = col_names
    char_name = char(name);
    if (contains(char_name, 'Time'))
        col_times{end+1} = char_name;
    end
end

time_only = data(:,col_times);
best_time = [];

for iter = 1:size(time_only, 1)
   row = time_only(iter,:);
   array = table2array(row);
   array2 = str2double(array);
   minimo = min(array2);
   idx = array2 == minimo;
   best_time = [best_time; idx];
end

sum_best_time = [];
for i = 1:size(best_test,2)
    sum_best_time = [sum_best_time, sum(best_time(:,i))];
end    

%%  Calcolo medie gen. per time


mean_time = [];

for iter = 1:size(time_only, 1)
   row = time_only(iter,:);
   array = table2array(row);
   array2 = str2double(array);
   media = mean(array2, 'omitnan');
   res = array2 / media;
   mean_time = [mean_time; array2 / media];
end

mean_mean_time = [];
for i = 1:size(mean_time,2)
    mean_mean_time = [mean_mean_time, mean(mean_time(:,i), 'omitnan')];
end  

mean_mean_time = (mean_mean_time - 1) * -100;

%% Numero timeout e errori
timeouts = [];
errors = [];
for i = 1:size(gen_names,2)
    array = table2array(time_only(:,i));
    timeouts = [timeouts, nnz(strcmp(array,'timeout'))];
    errors = [errors, nnz(strcmp(array,'null'))];
end

%%  Stampa risultati

result = table(gen_names',sum_best_test', sum_best_time', mean_mean_test', mean_mean_time', timeouts', errors');
result.Properties.VariableNames{1} = 'Generator';
result.Properties.VariableNames{2} = 'Best_tests';
result.Properties.VariableNames{3} = 'Best_time';
result.Properties.VariableNames{4} = 'Difference_from_test_mean';
result.Properties.VariableNames{5} = 'Difference_from_time_mean';
result.Properties.VariableNames{6} = 'Timeouts';
result.Properties.VariableNames{7} = 'Errors';

result

X = categorical(gen_names);
figure
bar(X, mean_mean_time)
ylabel('Difference of time'); 
ylim([-max(abs(mean_mean_time)),max(abs(mean_mean_time))]);
hold on
yyaxis right;
bar(X, mean_mean_test)
ylabel('Difference of #tests');

ax = gca;
ax.YColor = 'r';
yyaxis left;
ax.YColor = 'b';
darkGreen = [0, 0.6, 0];
ax.XColor = darkGreen;
ax.GridColor = 'y';
ax.GridAlpha = 0.9;

xlabel('Generators');
legend({'time','#tests'},'Location','northeast')
title('Difference from mean values [%]')


complexity = readtable('complexity.csv');
for i = 1:size(gen_names,2) 
    figure
    temp = strrep(table2array(time_only(:,i)),'timeout', '90000');
    time_array = str2double(temp);
    compl_array = table2array(complexity(:,3));
    scatter(compl_array(:,1), time_array(:,1));
    title(gen_names(:,i));
    xlabel('Complexity');
    ylabel('Generation time [ms]');
end

XXXX = 1:133;
for i = 1:size(gen_names,2) 
    figure
    temp = strrep(table2array(time_only(:,i)),'timeout', '90000');
    time_array = str2double(temp);
    time_array = sort(time_array);
    scatter(XXXX, time_array(:,1));
    title(gen_names(:,i));
    xlabel('Model');
    ylabel('Generation time [ms]');
end

figure
scatter(XXXX, sort(table2array(complexity(:,2))));
hold on
scatter(XXXX, sort(table2array(complexity(:,3))));
title("Models complexity");
xlabel('Model');
ylabel('Complexity');
legend({'# of parameters','# of constraints'},'Location','northeast');

param_and_const = table2array(complexity(:,2:3));
[~,pt] = sort(sum(param_and_const,2));
param_and_const = param_and_const(pt,:);
figure
bar(XXXX, param_and_const, 'stacked');
title("Models complexity");
xlabel('Model');
ylabel('Complexity');
legend({'# of parameters','# of constraints'},'Location','northeast');