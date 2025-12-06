import json
import os
import re
import argparse

def extract_words_by_length(input_file, output_dir):
    """
    从字典文件中提取指定长度的单词，并保存为7个JSON文件
    
    参数:
        input_file: 输入的字典文件路径
        output_dir: 输出目录路径
    """
    # 确保输出目录存在
    os.makedirs(output_dir, exist_ok=True)
    
    # 按长度分组的单词字典
    words_by_length = {length: [] for length in range(3, 10)}
    
    # 读取字典文件
    try:
        with open(input_file, 'r', encoding='utf-8') as file:
            for line in file:
                word = line.strip().lower()
                # 只保留全小写的英文字母单词
                if re.match('^[a-z]+$', word):
                    word_length = len(word)
                    if 3 <= word_length <= 9:
                        words_by_length[word_length].append(word)
    except FileNotFoundError:
        print(f"错误：找不到文件 '{input_file}'")
        exit(1)
    except Exception as e:
        print(f"读取文件时发生错误: {e}")
        exit(1)
    
    # 为每个长度生成JSON文件
    for length in range(3, 10):
        words_list = words_by_length[length]
        output_file = os.path.join(output_dir, f'words_{length}.json')
        
        # 创建JSON数据结构
        json_data = {
            "length": length,
            "count": len(words_list),
            "words": words_list
        }
        
        # 保存为JSON文件
        try:
            with open(output_file, 'w', encoding='utf-8') as f:
                json.dump(json_data, f, indent=2, ensure_ascii=False)
            print(f"已生成 {output_file} - 共 {len(words_list)} 个单词")
        except Exception as e:
            print(f"保存文件 {output_file} 时发生错误: {e}")
    
    print(f"\n处理完成！所有文件已保存到目录: {output_dir}")
    print("生成的文件列表:")
    for length in range(3, 10):
        print(f"  - words_{length}.json")

def main():
    parser = argparse.ArgumentParser(description='从字典文件中提取3-9长度的英文单词，生成7个JSON文件')
    parser.add_argument('--input', type=str, required=True,
                        help='输入的字典文件路径 (例如: words.txt)')
    parser.add_argument('--output', type=str, default='output',
                        help='输出目录 (默认: output)')
    parser.add_argument('--min-count', type=int, default=0,
                        help='只保留单词数量大于等于此值的文件 (默认: 0)')
    
    args = parser.parse_args()
    
    print("开始处理字典文件...")
    print(f"输入文件: {args.input}")
    print(f"输出目录: {args.output}")
    
    extract_words_by_length(args.input, args.output)

if __name__ == "__main__":
    main()